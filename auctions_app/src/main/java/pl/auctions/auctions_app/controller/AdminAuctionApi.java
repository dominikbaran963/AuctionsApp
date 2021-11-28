package pl.auctions.auctions_app.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


import pl.auctions.auctions_app.entity.Auction;
import pl.auctions.auctions_app.payload.response.MessageResponse;
import pl.auctions.auctions_app.repository.AuctionRepository;
import pl.auctions.auctions_app.repository.ClientRepository;
import pl.auctions.auctions_app.repository.ProvinceRepository;
import pl.auctions.auctions_app.repository.UserRepository;
@CrossOrigin(origins = "*", maxAge = 10000)
@RestController

public class AdminAuctionApi {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ClientRepository clientRepository;
	
	@Autowired
	ProvinceRepository provinceRepository;
	
	@Autowired
	AuctionRepository auctionRepository;
	
	@Autowired
	EntityManager em;
	
	@GetMapping(path = "/api/admin/auctions")
	public ResponseEntity<?> allAuctions() throws Exception, ParseException {
		List<Auction> auctionList = auctionRepository.findAll();
		JSONArray ja = new JSONArray();
		for(Auction auction : auctionList) {
			JSONObject jo = new JSONObject();
			jo.put("auctionId", auction.getIdAukcji());
			jo.put("auctionName", auction.getNazwaAukcji());
			jo.put("auctionStatus", auction.getAuctionStatus().getStan());
			jo.put("startDate", auction.getDataRozpoczecia());
			jo.put("endDate", auction.getDataZakonczenia());
			jo.put("buyNowPrice", auction.getCenaBlyskawiczna());
			jo.put("startPrice", auction.getCenaWywolawcza()); 
			jo.put("currentPrice", (Double) em.createNativeQuery(
					"SELECT IF((SELECT max(oferty.kwota) FROM oferty WHERE oferty.idAukcji = a.idAukcji) is null, a.cenaWywolawcza, (SELECT max(oferty.kwota) FROM oferty WHERE oferty.idAukcji = a.idAukcji)) FROM aukcja a WHERE a.idAukcji = "
							+ auction.getIdAukcji())
					.getSingleResult());
			
			jo.put("category", auction.getProduct().getCategory().getKategoria());
			jo.put("desc", auction.getProduct().getOpis()); 
			jo.put("condition", auction.getProduct().getStan()); 
			
			jo.put("category", auction.getProduct().getCategory().getKategoria());
			jo.put("desc", auction.getProduct().getOpis()); 
			jo.put("condition", auction.getProduct().getStan()); 
			
			jo.put("clientId", auction.getClientAuction().getClient().getIdKlienta());
			jo.put("town", auction.getClientAuction().getClient().getMiejscowosc()); 
			jo.put("province", auction.getClientAuction().getClient().getWojewodztwo().getNazwa()); 
			jo.put("phoneNumber", auction.getClientAuction().getClient().getNrTelefonu()); 
			jo.put("email", auction.getClientAuction().getClient().getUser().getEmail()); 
			
			ja.put(jo);
		}
		return ResponseEntity.ok(ja.toString());
	}
	
	@PostMapping(path = "/api/admin/auctions/{auctionId}/end")
	public ResponseEntity<?> closeAuction(@PathVariable("auctionId") Long auctionId) throws Exception, ParseException {
		Optional<Auction> auctionOptional = auctionRepository.findById(auctionId);
		
		if(!auctionOptional.isPresent())
			return (ResponseEntity<?>) ResponseEntity.of(auctionOptional);
		Auction auction = auctionOptional.get();
		
		auction.setIdStanuAukcji(1);
		auction.setDataZakonczenia(new Date());
		
		auctionRepository.save(auction);
		
		return ResponseEntity.ok(new MessageResponse("Zmiany zapisane pomyślnie"));
	}
	
	@PostMapping(path = "/api/admin/auctions/{auctionId}/start")
	public ResponseEntity<?> startAuction(@PathVariable("auctionId") Long auctionId) throws Exception, ParseException {
		Optional<Auction> auctionOptional = auctionRepository.findById(auctionId);
		
		if(!auctionOptional.isPresent())
			return (ResponseEntity<?>) ResponseEntity.of(auctionOptional);
		Auction auction = auctionOptional.get();
		
		Date now = new Date();
		Date nowPlus7Days = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(nowPlus7Days); 
		c.add(Calendar.DATE, 7);
		nowPlus7Days = c.getTime();
		
		
		auction.setIdStanuAukcji(2);
		auction.setDataZakonczenia(nowPlus7Days);
		auction.setDataRozpoczecia(now);
		
		auctionRepository.save(auction);
		
		return ResponseEntity.ok(new MessageResponse("Zmiany zapisane pomyślnie"));
	}
}
