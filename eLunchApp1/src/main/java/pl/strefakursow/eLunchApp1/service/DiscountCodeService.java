package pl.strefakursow.eLunchApp1.service;

import pl.strefakursow.eLunchApp1.DTO.DelivererDTO;
import pl.strefakursow.eLunchApp1.DTO.DiscountCodeDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DiscountCodeService {
    List<DiscountCodeDTO> getAll();
    void put(UUID uuid, DiscountCodeDTO discountCodeDTO);
    void delete(UUID uuid);
    Optional<DiscountCodeDTO> getByUuid(UUID uuid);
}
