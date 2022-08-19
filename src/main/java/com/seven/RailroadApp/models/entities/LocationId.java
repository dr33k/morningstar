package com.seven.RailroadApp.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
//Composite primary Key class
@Data
@Validated
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class LocationId implements Serializable {

    @NotBlank(message = "Required field")
    @Pattern(regexp = "^[A-Z]{4}$", message = "Incorrect format. Must be four(4) letters from A-Z (not a-z)")
    private String stateCode;
    @NotBlank(message = "Required field")
    @Pattern(regexp = "^[0-9]{2}$",message = "Incorrect format. Must be a two(2) digit number: 01,02 ...")
    private String stationNo;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
