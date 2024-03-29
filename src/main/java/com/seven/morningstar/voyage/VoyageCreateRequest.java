package com.seven.morningstar.voyage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.seven.morningstar.location.LocationId;
import com.seven.morningstar.AppRequest;
import jakarta.validation.constraints.Future;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.ZonedDateTime;

@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoyageCreateRequest implements AppRequest {
    private LocationId departureLocationId;
    private LocationId arrivalLocationId;
    @Future(message = "Past and current dates not allowed")
    private ZonedDateTime departureDateTime;
}
