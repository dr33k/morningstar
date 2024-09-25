package com.seven.morningstar.backend.booking;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.seven.morningstar.backend.location.enums.BookingStatus;
import com.seven.morningstar.backend.AppRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BookingUpdateRequest implements AppRequest {
    private UUID bookingNo;
    @NotBlank(message = "Required field",groups = BookingStatus.class)
    private BookingStatus status;
}
