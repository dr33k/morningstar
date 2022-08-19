package com.seven.RailroadApp.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.seven.RailroadApp.models.enums.VoyageStatus;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.UUID;

@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoyageUpdateRequest {
    private UUID voyageNo;
    private VoyageStatus status;
    private LocalDateTime arrivalDateTime;
}
