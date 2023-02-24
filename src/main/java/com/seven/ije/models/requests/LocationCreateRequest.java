package com.seven.ije.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.seven.ije.models.enums.StateCode;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
@Validated
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationCreateRequest implements AppRequest {
    @NotBlank(message = "Required field")
    private String stationName;
    @NotBlank(message = "Required field",groups = StateCode.class)
    private StateCode stateCode;
}
