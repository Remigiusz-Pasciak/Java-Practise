package pl.strefakursow.eLunchApp1.repo;


import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.strefakursow.eLunchApp1.model.Ingredient;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface IngredientRepo extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findByUuid(UUID uuid);
}
