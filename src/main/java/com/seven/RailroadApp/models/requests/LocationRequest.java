package com.seven.RailroadApp.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.seven.RailroadApp.models.entities.LocationId;
import com.seven.RailroadApp.models.enums.StateName;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationRequest {
    private LocationId locationId;
    @NotBlank(message = "Required field")
    private String stationName;
    @NotBlank(message = "Required field",groups = StateName.class)
    private StateName stateName;
}
