package pl.strefakursow.eLunchApp1.service;

import pl.strefakursow.eLunchApp1.DTO.DelivererDTO;
import pl.strefakursow.eLunchApp1.DTO.IngredientDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IngredientService {
    List<IngredientDTO> getAll();
    void put(UUID uuid, IngredientDTO ingredientDTO);
    void delete(UUID uuid);
    Optional<IngredientDTO> getByUuid(UUID uuid);
}
