package com.seven.ije.repositories;

import com.seven.ije.models.entities.Location;
import com.seven.ije.models.entities.LocationId;
import com.seven.ije.models.enums.LocationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("locationRepository")
public interface LocationRepository extends JpaRepository <Location, LocationId>{
    Integer countByLocationIdStateCode(String stateCode);
    Integer deleteByLocationIdAndStatus(LocationId id, LocationStatus status);
    Optional <Location> findByLocationIdAndStatusIn(LocationId locationId, List statuses);
}
