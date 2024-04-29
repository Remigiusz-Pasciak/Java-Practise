package pl.strefakursow.eLunchApp1.repo;


import org.springframework.stereotype.Repository;
import pl.strefakursow.eLunchApp1.model.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface DeliveryAddressRepo extends JpaRepository<DeliveryAddress, Long> {
    Optional<DeliveryAddress> findByUuid(UUID uuid);
}
