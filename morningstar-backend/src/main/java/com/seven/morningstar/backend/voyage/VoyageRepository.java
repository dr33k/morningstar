package com.seven.morningstar.backend.voyage;

import com.seven.morningstar.backend.location.enums.VoyageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VoyageRepository extends JpaRepository<Voyage, UUID> {
    Integer deleteByVoyageNoAndStatusAndPublished(UUID voyageNo, VoyageStatus voyageStatus, Boolean published);
    Optional<Voyage> findByVoyageNoAndPublished(UUID voyageNo, Boolean published);
}
