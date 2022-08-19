package com.seven.RailroadApp.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.seven.RailroadApp.models.entities.LocationId;
import com.seven.RailroadApp.models.enums.VoyageStatus;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoyageCreateRequest {
    private LocationId departureLocationId;
    private LocationId arrivalLocationId;
    @Future(message = "Past and current dates not allowed")
    private LocalDate travelDate;
    private LocalTime travelTime;
}
