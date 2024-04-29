package pl.strefakursow.eLunchApp1.repo;


import org.springframework.stereotype.Repository;
import pl.strefakursow.eLunchApp1.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface RestaurantRepo extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByUuid(UUID uuid);
}
