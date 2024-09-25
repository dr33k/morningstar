package com.seven.morningstar.backend.location;

import com.seven.morningstar.backend.location.enums.LocationStatus;
import com.seven.morningstar.backend.location.enums.StateCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.Optional;

@ApplicationScope
@Repository
public interface LocationRepository extends JpaRepository <Location, LocationId>{
    Integer countByLocationIdStateCode(StateCode stateCode);
    Integer deleteByLocationIdAndStatus(LocationId id, LocationStatus status);
    Optional <Location> findByLocationIdAndStatusIn(LocationId locationId, List statuses);
}
