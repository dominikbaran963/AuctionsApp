package pl.auctions.auctions_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.auctions.auctions_app.entity.Offers;

@Repository
public interface OffertsRepository extends JpaRepository<Offers, Long>{

}
