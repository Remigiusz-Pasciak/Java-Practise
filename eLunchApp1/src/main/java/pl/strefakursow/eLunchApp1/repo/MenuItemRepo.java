package pl.strefakursow.eLunchApp1.repo;


import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.strefakursow.eLunchApp1.model.MenuItem;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface MenuItemRepo extends JpaRepository<MenuItem, Long> {
    Optional<MenuItem> findByUuid(UUID uuid);
}
