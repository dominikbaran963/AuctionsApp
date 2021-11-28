package pl.auctions.auctions_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.auctions.auctions_app.entity.Province;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
	Optional<Province> findByNazwa(String name);
}
