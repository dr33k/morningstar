package com.seven.morningstar.booking;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.seven.morningstar.enums.SeatType;
import com.seven.morningstar.AppRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

    @Validated
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    public class BookingCreateRequest implements AppRequest {
        private UUID voyageNo;
        @NotBlank(message = "Required field", groups = SeatType.class)
        private SeatType seatType;
    }



