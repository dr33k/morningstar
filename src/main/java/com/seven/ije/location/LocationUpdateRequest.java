package com.seven.ije.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.seven.ije.enums.LocationStatus;
import com.seven.ije.AppRequest;
import jakarta.validation.constraints.Null;
import lombok.Data;
import org.springframework.lang.Nullable;
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
