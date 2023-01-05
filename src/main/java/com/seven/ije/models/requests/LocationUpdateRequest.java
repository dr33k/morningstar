package com.seven.ije.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.seven.ije.models.entities.LocationId;
import com.seven.ije.models.enums.LocationStatus;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationUpdateRequest implements AppRequest {
    private LocationId locationId;
    private String stationName;
    private LocationStatus status;
}
