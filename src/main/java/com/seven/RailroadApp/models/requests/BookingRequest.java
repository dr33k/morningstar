package com.seven.RailroadApp.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.seven.RailroadApp.models.entities.Location;
import com.seven.RailroadApp.models.enums.SeatType;
import com.seven.RailroadApp.models.enums.TravelTime;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BookingRequest {
    @NotNull(message = "Required field")
    private Location departureLocation;
    @NotNull(message = "Required field")
    private Location arrivalLocation;
    @NotNull(message = "Required field")
    @Future(message = "Past and current dates not allowed",groups = LocalDate.class)
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
    private LocalDate travelDate;
    @NotNull(message = "Required field",groups = TravelTime.class)
    @Pattern(regexp = "^\\d{1,2}:\\d{2}")
    private TravelTime travelTime;
    @NotNull(message = "Required field",groups = SeatType.class)
    private SeatType seatType;
}
