package pl.auctions.auctions_app.controller;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.auctions.auctions_app.entity.Auction;
import pl.auctions.auctions_app.entity.WatchedAuctions;
import pl.auctions.auctions_app.entity.Offers;
import pl.auctions.auctions_app.entity.WinAuctions;
import pl.auctions.auctions_app.entity.Photo;
import pl.auctions.auctions_app.payload.request.AuctionBidRequest;
import pl.auctions.auctions_app.payload.request.AuctionBuyRequest;
import pl.auctions.auctions_app.payload.request.WatchedAuctionRequest;
import pl.auctions.auctions_app.payload.response.MessageResponse;
import pl.auctions.auctions_app.repository.AuctionRepository;
import pl.auctions.auctions_app.repository.ClientRepository;
import pl.auctions.auctions_app.repository.OffertsRepository;
import pl.auctions.auctions_app.repository.PhotoRepository;
import pl.auctions.auctions_app.repository.ProductRepository;
import pl.auctions.auctions_app.repository.ProvinceRepository;
import pl.auctions.auctions_app.repository.RoleRepository;
import pl.auctions.auctions_app.repository.UserRepository;
import pl.auctions.auctions_app.repository.WatchedAuctionRepository;
import pl.auctions.auctions_app.repository.WinAuctionsRepository;

@CrossOrigin(origins = "*", maxAge = 10000)
@RestController
public class AuctionsApi {
	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	ProvinceRepository provinceRepository;

	@Autowired
	ClientRepository clientRepository;

	@Autowired
	AuctionRepository auctionRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	PhotoRepository photoRepository;

	@Autowired
	OffertsRepository offersRepository;

	@Autowired
	WinAuctionsRepository winAuctionsRepository;

	@Autowired
	WatchedAuctionRepository watchedAuctionsRepository;

	@Autowired
	EntityManager em;

	@GetMapping(path = "/api/auction/{idAukcji}")
	public ResponseEntity<?> oneAuctionDetails(@PathVariable("idAukcji") Long idAukcji)
			throws Exception, ParseException {
		Optional<Auction> auctionOptional = auctionRepository.findById(idAukcji);
		if (!auctionOptional.isPresent())
			return (ResponseEntity<?>) ResponseEntity.of(auctionOptional);
		Auction auction = auctionOptional.get();
		JSONObject jo = new JSONObject();
		jo.put("auctionId", auction.getIdAukcji()); // idAukcji
		jo.put("auctiontitle", auction.getNazwaAukcji()); // tytuł
		jo.put("auctionState", auction.getIdStanuAukcji()); // wartość 1 gdy aktywny , 0 gdy zakończony
		jo.put("startDate", auction.getDataRozpoczecia()); // dataRozpoczecia
		jo.put("endDate", auction.getDataZakonczenia()); // dataZakonczenia
		jo.put("buyNowPrice", auction.getCenaBlyskawiczna()); // cena kup teraz
		Object amount = em.createNativeQuery("SELECT max(kwota) FROM oferty WHERE idAukcji = " + auction.getIdAukcji())
				.getSingleResult();
		if (amount == null) {
			amount = auction.getCenaWywolawcza();
		}
		jo.put("maxBidPrice", amount); // zalicywowana makwymalna kwota
		jo.put("category", auction.getProduct().getCategory().getKategoria()); // kategoria jako string "elektronika
																				// itp"
		jo.put("description", auction.getProduct().getOpis()); // opis
		jo.put("condition", auction.getProduct().getStan()); // nowy , używany
		jo.put("province", auction.getClientAuction().getClient().getWojewodztwo().getNazwa()); // wojewodztwo
		jo.put("location", auction.getClientAuction().getClient().getMiejscowosc()); // miejscowosc
		jo.put("authorEmail", auction.getClientAuction().getClient().getUser().getEmail());
		jo.put("authorTel", auction.getClientAuction().getClient().getNrTelefonu());
		jo.put("authorName", auction.getClientAuction().getClient().getUser().getName());
		jo.put("authorLastname", auction.getClientAuction().getClient().getUser().getLast_name());

		// zdjecia
		JSONArray ja = new JSONArray();
		for (Photo photo : auction.getProduct().getPhotos()) {
			ja.put(new String(photo.getZdjecie(), StandardCharsets.UTF_8));
		}

		jo.put("photos", ja);

		return ResponseEntity.ok(jo.toString());
	}

	@GetMapping(path = "/api/auction/current/price/{idAukcji}")
	public ResponseEntity<?> currentPrice(@PathVariable("idAukcji") Long idAukcji) throws Exception, ParseException {

		Optional<Auction> auctionOptional = auctionRepository.findById(idAukcji);
		if (!auctionOptional.isPresent())
			return (ResponseEntity<?>) ResponseEntity.of(auctionOptional);

		JSONObject jo = new JSONObject();
		jo.put("idAukcji", idAukcji);
		jo.put("price", (Double) em.createNativeQuery(
				"SELECT IF((SELECT max(oferty.kwota) FROM oferty WHERE oferty.idAukcji = a.idAukcji) is null, a.cenaWywolawcza, (SELECT max(oferty.kwota) FROM oferty WHERE oferty.idAukcji = a.idAukcji)) FROM aukcja a WHERE a.idAukcji = "
						+ idAukcji)
				.getSingleResult());
		return ResponseEntity.ok(jo.toString());
	}

	@PostMapping(path = "/api/auction/{auctionId}/bid")
	public ResponseEntity<?> auctionBid(@PathVariable("auctionId") Long auctionId, @RequestBody String request)
			throws Exception, ParseException {

		Optional<Auction> auctionOptional = auctionRepository.findById(auctionId);
		if (!auctionOptional.isPresent())
			return (ResponseEntity<?>) ResponseEntity.of(auctionOptional);

		AuctionBidRequest bidRequest = new ObjectMapper().readValue(request, AuctionBidRequest.class);

		Double kwota = (double) em.createNativeQuery("SELECT max(kwota) WHERE idAukcji = " + bidRequest.getAuctionId())
				.getFirstResult();
		if (kwota == null || kwota < bidRequest.getOffer()) {
			Offers offer = new Offers();
			offer.setData(new Date());
			offer.setIdAukcji(auctionId);
			offer.setIdKlienta(bidRequest.getUserId());
			offer.setIdAukcji(bidRequest.getAuctionId());
			offer.setKwota(bidRequest.getOffer());

			offersRepository.save(offer);

			return ResponseEntity.ok(new MessageResponse("Oferta została złożona"));
		} else
			return new ResponseEntity<>("Jest już wyższa oferta:", HttpStatus.BAD_REQUEST);
	}

	@PostMapping(path = "/api/auction/{auctionId}/buy")
	public ResponseEntity<?> auctionBuy(@PathVariable("auctionId") Long auctionId, @RequestBody String request)
			throws Exception, ParseException {
		AuctionBuyRequest buyRequest = new ObjectMapper().readValue(request, AuctionBuyRequest.class);
		Optional<Auction> auctionOptional = auctionRepository.findById(auctionId);
		if (!auctionOptional.isPresent())
			return (ResponseEntity<?>) ResponseEntity.of(auctionOptional);
		Auction auction = auctionOptional.get();

		Offers offer = new Offers();
		offer.setData(new Date());
		offer.setIdAukcji(auctionId);
		offer.setIdKlienta(buyRequest.getUserId());
		offer.setIdAukcji(buyRequest.getAuctionId());
		offer.setKwota(auction.getCenaBlyskawiczna());

		offersRepository.save(offer);

		auction.setDataZakonczenia(new Date());
		auction.setIdStanuAukcji(3);

		auctionRepository.save(auction);

		WinAuctions winAuction = new WinAuctions();
		winAuction.setIdAukcji(auctionId);
		winAuction.setIdKlienta(buyRequest.getUserId());

		winAuctionsRepository.save(winAuction);

		return ResponseEntity.ok(new MessageResponse("Przedmiot został zakupiony"));
	}

	@PostMapping(path = "/api/auctions/{userId}/favorite")
	public ResponseEntity<?> watchedAuction(@PathVariable("userId") Long userId, @RequestBody String request)
			throws Exception, ParseException {

		WatchedAuctionRequest watchedRequest = new ObjectMapper().readValue(request, WatchedAuctionRequest.class);

		WatchedAuctions watchedAuction = new WatchedAuctions();
		watchedAuction.setIdKlienta(userId);
		watchedAuction.setIdAukcji(watchedRequest.getAuctionId());
		watchedAuctionsRepository.save(watchedAuction);
		return ResponseEntity.ok(new MessageResponse("Aukcja została dodana do ulubionych"));
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(path = "/api/auctions/latest", params = { "page" })
	public ResponseEntity<?> latestAuctions(@RequestParam("page") int page) throws Exception, ParseException {
		int offset = (page - 1) * 12;
		Collection<Object> idsSql = em.createNativeQuery(
				"SELECT idAukcji FROM Aukcja WHERE idStanuAukcji = 2 ORDER BY idAukcji DESC LIMIT 12 OFFSET " + offset)
				.getResultList();

		List<Long> ids = new ArrayList<Long>();

		for (Object id : idsSql) {
			ids.add(Long.valueOf(id.toString()));
			System.out.println(id);
		}

		return ResponseEntity.ok(createJsonArray(ids));
	}
	
	@GetMapping(path = "/api/auctions/{userId}/favorit")
	public ResponseEntity<?> favoritAuctions(@PathVariable("userId") Long userId) throws Exception, ParseException {
		@SuppressWarnings("unchecked")
		List<Object> idsSql = em.createNativeQuery(
				"SELECT distinct a.idAukcji FROM Aukcja a JOIN obserwowane_aukcje oa ON a.idAukcji = oa.idAukcji WHERE a.idStanuAukcji = 2 AND oa.idKlienta = "
						+ userId)
				.getResultList();
		List<Long> ids = new ArrayList<Long>();

		for (Object id : idsSql) {
			ids.add(Long.valueOf(id.toString()));
		}

		return ResponseEntity.ok(createJsonArray(ids));
	}

	@GetMapping(path = "/api/auctions/{userId}/own")
	public ResponseEntity<?> ownAuctions(@PathVariable("userId") Long userId) throws Exception, ParseException {
		@SuppressWarnings("unchecked")
		List<Object> idsSql = em.createNativeQuery(
				"SELECT distinct a.idAukcji FROM Aukcja a JOIN klient_aukcja ka ON a.idAukcji = ka.idAukcji WHERE ka.idKlienta = "
						+ userId)
				.getResultList();
		List<Long> ids = new ArrayList<Long>();

		for (Object id : idsSql) {
			ids.add(Long.valueOf(id.toString()));
		}

		return ResponseEntity.ok(createJsonArray(ids));
	}

	@GetMapping(path = "/api/auctions/{userId}/win")
	public ResponseEntity<?> winAuctions(@PathVariable("userId") Long userId) throws Exception, ParseException {
		@SuppressWarnings("unchecked")
		List<Object> idsSql = em.createNativeQuery(
				"SELECT distinct a.idAukcji FROM Aukcja a JOIN wygrane_aukcje wa ON a.idAukcji = wa.idAukcji WHERE wa.idKlienta = "
						+ userId)
				.getResultList();
		List<Long> ids = new ArrayList<Long>();

		for (Object id : idsSql) {
			ids.add(Long.valueOf(id.toString()));
		}

		return ResponseEntity.ok(createJsonArray(ids));
	}

	@RequestMapping(path = "/api/auctions/{text}")
	public ResponseEntity<?> auctions(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "min", required = false) Integer min,
			@RequestParam(value = "max", required = false) Integer max,
			@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "province", required = false) String province,
			@RequestParam(value = "sort", required = false) String sort,
			@PathVariable(value = "text", required = false) String searchText) throws Exception, ParseException {
		return ResponseEntity.ok(createJsonArray(searchSqlGenerator(searchText, page, min, max, category, province, sort)));
	}

	@RequestMapping(path = "/api/auctions")
	public ResponseEntity<?> auctionsWithoutSearch(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "min", required = false) Integer min,
			@RequestParam(value = "max", required = false) Integer max,
			@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "province", required = false) String province,
			@RequestParam(value = "sort", required = false) String sort) throws Exception, ParseException {
		return ResponseEntity.ok(createJsonArray(searchSqlGenerator(null, page, min, max, category, province, sort)));
	}
	
	public List<Long> searchSqlGenerator(String searchText,Integer page , Integer min, Integer max, String category , String province, String sort ) {
		int offset = (page - 1) * 12;

		StringBuilder sb = new StringBuilder();
		sb.append(
				"SELECT a.idAukcji FROM Aukcja a JOIN Produkt p ON a.idProduktu = p.idProduktu JOIN kategoria k ON k.idKategorii = p.idKategorii JOIN klient_aukcja ka ON ka.idAukcji = a.idAukcji JOIN klient kl ON ka.idKlienta = kl.idKlienta JOIN wojewodztwo w ON w.idWojewodztwa = kl.idWojewodztwa WHERE a.idStanuAukcji = 2");
		if (min != null) {
			sb.append(
					" AND (SELECT IF((SELECT max(oferty.kwota) FROM oferty WHERE oferty.idAukcji = a.idAukcji) is null, a.cenaWywolawcza, (SELECT max(oferty.kwota) FROM oferty WHERE oferty.idAukcji = a.idAukcji))) >= ");
			sb.append(min.toString());
		}
		if (max != null) {
			sb.append(
					" AND (SELECT IF((SELECT max(oferty.kwota) FROM oferty WHERE oferty.idAukcji = a.idAukcji) is null, a.cenaWywolawcza, (SELECT max(oferty.kwota) FROM oferty WHERE oferty.idAukcji = a.idAukcji))) <= ");
			sb.append(max.toString());
		}
		if (category != null) {
			sb.append(" AND k.kategoria = '");
			sb.append(category);
			sb.append("' ");
		}
		if (searchText != null) {
			searchText = searchText.replaceAll("-", " ");
			sb.append(" AND a.nazwaAukcji like '%");
			sb.append(searchText);
			sb.append("%' ");
		}
		if (province != null) {
			province = province.replaceAll("-", " ");
			sb.append(" AND w.nazwa = '");
			sb.append(province);
			sb.append("' ");
		}
		if (sort != null) {
			switch (sort) {
			case "latest":
				sb.append(" ORDER BY a.dataRozpoczecia DESC ");
				break;
			case "asc":
				sb.append(
						" ORDER BY (SELECT IF((SELECT max(oferty.kwota) FROM oferty WHERE oferty.idAukcji = a.idAukcji) is null, a.cenaWywolawcza, (SELECT max(oferty.kwota) FROM oferty WHERE oferty.idAukcji = a.idAukcji))) ASC ");
				break;
			case "desc":
				sb.append(
						" ORDER BY (SELECT IF((SELECT max(oferty.kwota) FROM oferty WHERE oferty.idAukcji = a.idAukcji) is null, a.cenaWywolawcza, (SELECT max(oferty.kwota) FROM oferty WHERE oferty.idAukcji = a.idAukcji))) DESC ");
				break;
			default:
				break;
			}
		}
		sb.append(" LIMIT 12 OFFSET ");
		sb.append(offset);

		@SuppressWarnings("unchecked")
		List<Object> idsSql = em.createNativeQuery(sb.toString()).getResultList();

		List<Long> ids = new ArrayList<Long>();

		for (Object id : idsSql) {
			ids.add(Long.valueOf(id.toString()));
		}
		return ids;
	}

	public String createJsonArray(List<Long> auctions) {
		JSONArray ja = new JSONArray();
		for (Long auctionId : auctions) {
			Optional<Auction> auctionOptional = auctionRepository.findById(auctionId);
			Auction auction = auctionOptional.get();
			JSONObject jo = new JSONObject();
			jo.put("auctionId", auction.getIdAukcji()); // idAukcji
			jo.put("auctiontitle", auction.getNazwaAukcji()); // tytuł
			jo.put("auctionState", auction.getIdStanuAukcji()); // wartość 1 gdy aktywny , 0 gdy zakończony
			jo.put("startDate", auction.getDataRozpoczecia()); // dataRozpoczecia
			jo.put("endDate", auction.getDataZakonczenia()); // dataZakonczenia
			jo.put("buyNowPrice", auction.getCenaBlyskawiczna()); // cena kup teraz
			Object amount = em
					.createNativeQuery("SELECT max(kwota) FROM oferty WHERE idAukcji = " + auction.getIdAukcji())
					.getSingleResult();
			if (amount == null) {
				amount = auction.getCenaWywolawcza();
			}
			jo.put("maxBidPrice", amount); // zalicywowana makwymalna kwota
			jo.put("category", auction.getProduct().getCategory().getKategoria()); // kategoria jako string "elektronika
																					// itp"
			jo.put("description", auction.getProduct().getOpis()); // opis
			jo.put("condition", auction.getProduct().getStan()); // nowy , używany
			Object idZdjecia = em
					.createNativeQuery(
							"SELECT idZdjecia FROM zdjecie WHERE idProduktu = " + auction.getIdProduktu() + " LIMIT 1")
					.getSingleResult();
			if (idZdjecia != null) {
				Optional<Photo> photo = photoRepository.findById(Long.valueOf(idZdjecia.toString()));
				if (photo.isPresent()) {
					jo.put("photo", new String(photo.get().getZdjecie(), StandardCharsets.UTF_8)); // jedna fotka
				}
			}
			jo.put("province", auction.getClientAuction().getClient().getWojewodztwo().getNazwa()); // wojewodztwo
			jo.put("location", auction.getClientAuction().getClient().getMiejscowosc()); // miejscowosc

			jo.put("firstname", auction.getClientAuction().getClient().getUser().getName()); // imie
			jo.put("phoneNumber", auction.getClientAuction().getClient().getNrTelefonu()); // numerTel
			jo.put("email", auction.getClientAuction().getClient().getUser().getEmail()); // email

			ja.put(jo);
		}
		return ja.toString();
	}

}