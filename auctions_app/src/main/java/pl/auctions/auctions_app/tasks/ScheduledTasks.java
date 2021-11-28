package pl.auctions.auctions_app.tasks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pl.auctions.auctions_app.entity.Auction;
import pl.auctions.auctions_app.entity.WinAuctions;
import pl.auctions.auctions_app.repository.AuctionRepository;
import pl.auctions.auctions_app.repository.ClientRepository;
import pl.auctions.auctions_app.repository.OffertsRepository;
import pl.auctions.auctions_app.repository.ProductRepository;
import pl.auctions.auctions_app.repository.WatchedAuctionRepository;
import pl.auctions.auctions_app.repository.WinAuctionsRepository;

@Component
public class ScheduledTasks {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Autowired
	ClientRepository clientRepository;

	@Autowired
	AuctionRepository auctionRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	OffertsRepository offersRepository;
	
	@Autowired
	WatchedAuctionRepository watchedAuctionRepository;

	@Autowired
	WinAuctionsRepository winAuctionsRepository;

	@Autowired
	EntityManager em;
	
	@Scheduled(fixedRate = 5000)
	public void reportCurrentTime() {
		Date now = new Date();
		log.info("Auction task started", dateFormat.format(new Date()));

		/*
		 * sprawdzam czy aukcja się zakończta
		 */

		List<Auction> endActions = new ArrayList<Auction>();
		for (Auction auction : auctionRepository.findAll()) {
			if (auction.getDataZakonczenia().before(now)) {
				endActions.add(auction);
			}
		}

		/*
		 * Obsługa skończonych aukcji
		 */

		for (Auction auction : endActions) {
			/*
			 * maksymalna oferta
			 */
			@SuppressWarnings("unchecked")
			Object userId = em
					.createNativeQuery("SELECT idKlienta FROM oferty WHERE idAukcji = " + auction.getIdAukcji())
					.getResultList()
					.stream().findFirst().orElse(null);
			if (userId == null) {
				auction.setIdStanuAukcji(1);// nie sprzedana
				auctionRepository.save(auction);
				
			} else {
				auction.setIdStanuAukcji(3); // aukcja sprzedana
				WinAuctions winAuction = new WinAuctions();
				winAuction.setIdKlienta(Long.valueOf(userId.toString()));
				winAuction.setIdAukcji(auction.getIdAukcji());
				watchedAuctionRepository.deleteAll(watchedAuctionRepository.findAllByIdAukcji(auction.getIdAukcji()));
				auctionRepository.save(auction);
				winAuctionsRepository.save(winAuction);
			}
			log.info("Auction task finish", dateFormat.format(new Date()));
		}
	}
}