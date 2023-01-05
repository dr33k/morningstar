package com.seven.ije.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.seven.ije.models.enums.VoyageStatus;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoyageUpdateRequest implements AppRequest{
    private UUID voyageNo;
    private VoyageStatus status;
}
