package com.backbase.model.repository;

import com.backbase.model.entity.ServiceAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceAccessRepo extends JpaRepository<ServiceAccess, Long> {
}