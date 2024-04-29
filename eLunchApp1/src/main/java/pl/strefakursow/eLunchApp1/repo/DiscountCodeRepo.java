package pl.strefakursow.eLunchApp1.repo;


import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.strefakursow.eLunchApp1.model.DiscountCode;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface DiscountCodeRepo extends JpaRepository<DiscountCode, Long> {
    Optional<DiscountCode> findByUuid(UUID uuid);
}

