package com.spms.parkingservice.repo;

import com.spms.parkingservice.entity.Parking;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ParkingRepo extends JpaRepository<Parking, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM parking p WHERE p.id = :id")
    Optional<Parking> findByIdForUpdate(@Param("id") Long id);
}
