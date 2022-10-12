package com.seven.railroadapp.repositories;

import com.seven.railroadapp.models.entities.Location;
import com.seven.railroadapp.models.entities.LocationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository <Location, LocationId>{
    Integer countByLocationIdStateCode(String stateCode);
}
