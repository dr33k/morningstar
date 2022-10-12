package com.seven.railroadapp.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.seven.railroadapp.models.entities.LocationId;
import com.seven.railroadapp.models.enums.LocationStatus;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationUpdateRequest {
    private LocationId locationId;
    private String stationName;
    private LocationStatus status;
}
