package com.seven.ije.repositories;

import com.seven.ije.models.entities.Location;
import com.seven.ije.models.entities.LocationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository <Location, LocationId>{
    Integer countByLocationIdStateCode(String stateCode);
}
