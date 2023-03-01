package com.seven.ije.location;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.seven.ije.enums.StateCode;
import com.seven.ije.AppRequest;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
@Validated
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationCreateRequest implements AppRequest {
    @NotBlank(message = "Required field")
    private String stationName;
    @NotBlank(message = "Required field",groups = StateCode.class)
    @Enumerated(EnumType.STRING)
    private StateCode stateCode;
}
