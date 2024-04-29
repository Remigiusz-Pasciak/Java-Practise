package pl.strefakursow.eLunchApp1.repo;


import org.springframework.stereotype.Repository;
import pl.strefakursow.eLunchApp1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUuid(UUID uuid);
}
