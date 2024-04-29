package pl.strefakursow.eLunchApp1.repo;


import org.springframework.stereotype.Repository;
import pl.strefakursow.eLunchApp1.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    Optional<Product> findByUuid(UUID uuid);
}
