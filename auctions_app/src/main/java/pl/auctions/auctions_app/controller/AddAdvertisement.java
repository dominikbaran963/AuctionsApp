package pl.auctions.auctions_app.controller;


import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.auctions.auctions_app.entity.Auction;
import pl.auctions.auctions_app.entity.Category;
import pl.auctions.auctions_app.entity.ClientAuction;
import pl.auctions.auctions_app.entity.Product;
import pl.auctions.auctions_app.entity.Photo;
import pl.auctions.auctions_app.payload.request.AddAdvertisementRequest;
import pl.auctions.auctions_app.payload.response.MessageResponse;
import pl.auctions.auctions_app.repository.AuctionRepository;
import pl.auctions.auctions_app.repository.CategoryRepository;
import pl.auctions.auctions_app.repository.ClientAuctionRepository;
import pl.auctions.auctions_app.repository.PhotoRepository;
import pl.auctions.auctions_app.repository.ProductRepository;
import pl.auctions.auctions_app.security.jwt.JwtUtils;

@CrossOrigin(origins = "*", maxAge = 10000)
@RestController
public class AddAdvertisement {
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	PhotoRepository photoRepository;
	
	@Autowired
	AuctionRepository auctionRepository;
	
	@Autowired
	ClientAuctionRepository clienAuctionRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	@PostMapping("/api/auctions/create")
	public ResponseEntity<?> authenticateUser(@RequestBody String request) throws Exception, ParseException {
		AddAdvertisementRequest advertisementRequest = 
				new ObjectMapper().readValue(request, AddAdvertisementRequest.class);
		
		//insert produktu
		Product produkt = new Product();
		produkt.setNazwa(advertisementRequest.getTitle());
		produkt.setOpis(advertisementRequest.getDescription());
		produkt.setStan(advertisementRequest.getState());
		Optional<Category> kategoria = categoryRepository.findByKategoria(advertisementRequest.getCategory());
		produkt.setIdKategorii(kategoria.get().getIdKategorii());
		productRepository.save(produkt);		
		
		//insert zdjęć 
		for(int i = 0 ; i < advertisementRequest.getPhotosLength() ; i++ ) {
			Photo zdj = new Photo();
			zdj.setIdProduktu(produkt.getIdProduktu());
			zdj.setZdjecie(advertisementRequest.getPhotosBlob()[i].getBytes(StandardCharsets.UTF_8));
			photoRepository.save(zdj);
		}
		
		//insert aukcji
		Auction auction = new Auction();
		auction.setCenaBlyskawiczna(advertisementRequest.getInstantPrice());
		auction.setCenaWywolawcza(advertisementRequest.getStartingPrice());
		Date date = new Date();
		auction.setDataRozpoczecia(date);
		Date dt = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt); 
		c.add(Calendar.DATE, 7);
		dt = c.getTime();
		auction.setDataZakonczenia(dt);
		auction.setIdProduktu(produkt.getIdProduktu());
		auction.setIdStanuAukcji(2);
		auction.setNazwaAukcji(advertisementRequest.getTitle());
		auctionRepository.save(auction);
		
		ClientAuction klientAukcja = new ClientAuction();
		klientAukcja.setIdAukcji(auction.getIdAukcji());
		klientAukcja.setIdKlienta(advertisementRequest.getUserId());
		clienAuctionRepository.save(klientAukcja);
		
		return ResponseEntity.ok(new MessageResponse("Advertisement added!"));
	}
	
}
