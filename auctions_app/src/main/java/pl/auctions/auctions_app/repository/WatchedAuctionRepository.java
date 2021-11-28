package pl.auctions.auctions_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.auctions.auctions_app.entity.WatchedAuctions;

@Repository
public interface WatchedAuctionRepository extends JpaRepository<WatchedAuctions, Long> {
	List<WatchedAuctions> findAllByIdAukcji(Long idAukcji);
}
