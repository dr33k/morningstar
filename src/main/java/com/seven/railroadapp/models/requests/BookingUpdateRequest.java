package com.seven.railroadapp.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.seven.railroadapp.models.enums.BookingStatus;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BookingUpdateRequest {
    private UUID bookingNo;
    @NotBlank(message = "Required field",groups = BookingStatus.class)
    private BookingStatus status;
}
