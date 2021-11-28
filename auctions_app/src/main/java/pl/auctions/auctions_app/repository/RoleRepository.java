package pl.auctions.auctions_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.auctions.auctions_app.entity.ERole;
import pl.auctions.auctions_app.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByRole(ERole name);
}
