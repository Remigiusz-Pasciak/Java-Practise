package pl.strefakursow.eLunchApp1.repo;


import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.strefakursow.eLunchApp1.model.Dish;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface DishRepo extends JpaRepository<Dish, Long> {
    Optional<Dish> findByUuid(UUID uuid);
}
