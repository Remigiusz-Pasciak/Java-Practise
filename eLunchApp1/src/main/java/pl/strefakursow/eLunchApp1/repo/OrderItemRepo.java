package pl.strefakursow.eLunchApp1.repo;


import org.springframework.stereotype.Repository;
import pl.strefakursow.eLunchApp1.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByUuid(UUID uuid);
}
