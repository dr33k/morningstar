package com.seven.railroadapp.repositories;

import com.seven.railroadapp.models.entities.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VoyageRepository extends JpaRepository<Voyage, UUID> {
}
