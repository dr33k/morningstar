package com.seven.morningstar.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.seven.morningstar.location.enums.LocationStatus;
import com.seven.morningstar.AppRequest;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationUpdateRequest implements AppRequest {
    @JsonIgnore
    private LocationId locationId;

    private String stationName;

    private LocationStatus status;
}
