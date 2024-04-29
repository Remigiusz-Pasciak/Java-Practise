package pl.strefakursow.eLunchApp1.repo;


import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.strefakursow.eLunchApp1.model.OpenTime;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface OpenTimeRepo extends JpaRepository<OpenTime, Long> {
    Optional<OpenTime> findByUuid(UUID uuid);
}
