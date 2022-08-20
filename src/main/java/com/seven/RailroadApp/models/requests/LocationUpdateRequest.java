package com.seven.RailroadApp.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.seven.RailroadApp.models.entities.LocationId;
import com.seven.RailroadApp.models.enums.LocationStatus;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotBlank;

@Validated
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationUpdateRequest {
    private LocationId locationId;
    private String stationName;
    private LocationStatus status;
}
