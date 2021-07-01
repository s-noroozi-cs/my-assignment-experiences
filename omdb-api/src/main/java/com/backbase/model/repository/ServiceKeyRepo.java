package com.backbase.model.repository;

import com.backbase.model.entity.ServiceKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ServiceKeyRepo extends JpaRepository<ServiceKey, Long> {

    Optional<ServiceKey> findByKeyAndExpirationTimeAfter(String key, LocalDateTime currentTime);
}
