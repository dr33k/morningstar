package com.seven.ije.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.seven.ije.models.entities.LocationId;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoyageCreateRequest implements AppRequest {
    private LocationId departureLocationId;
    private LocationId arrivalLocationId;
    @Future(message = "Past and current dates not allowed")
    private LocalDateTime travelDateTime;
}
