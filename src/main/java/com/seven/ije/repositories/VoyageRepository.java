package com.seven.ije.repositories;

import com.seven.ije.models.entities.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VoyageRepository extends JpaRepository<Voyage, UUID> {
    Integer deleteByIdAndStatus(UUID voyageNo, String voyageStatus);
}
