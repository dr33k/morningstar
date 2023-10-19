package com.seven.morningstar.location;

import com.seven.morningstar.enums.LocationStatus;
import com.seven.morningstar.enums.StateCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.Optional;

@Repository("locationRepository")
@ApplicationScope
public interface LocationRepository extends JpaRepository <Location, LocationId>{
    Integer countByLocationIdStateCode(StateCode stateCode);
    Integer deleteByLocationIdAndStatus(LocationId id, LocationStatus status);
    Optional <Location> findByLocationIdAndStatusIn(LocationId locationId, List statuses);
}
