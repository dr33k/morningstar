package com.seven.morningstar.backend.voyage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.seven.morningstar.backend.location.enums.VoyageStatus;
import com.seven.morningstar.backend.AppRequest;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.ZonedDateTime;
import java.util.UUID;

@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoyageUpdateRequest implements AppRequest {
    private UUID voyageNo;
    private VoyageStatus status;
    private ZonedDateTime travelDateTime;
    private Boolean published;
}
