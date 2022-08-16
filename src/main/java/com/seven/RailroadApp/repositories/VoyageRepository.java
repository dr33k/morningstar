package com.seven.RailroadApp.repositories;

import com.seven.RailroadApp.models.entities.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VoyageRepository extends JpaRepository<Voyage, UUID> {
    Iterable<Voyage> findAllByPassenger(Long passengerId);
}
