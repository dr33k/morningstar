package com.seven.morningstar.location;

import com.seven.morningstar.enums.StateCode;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
//Composite primary Key class
@Data
@Validated
@Embeddable
@NoArgsConstructor
@ToString
public class LocationId implements Serializable {

    @NotBlank(message = "The state code is a required field", groups = StateCode.class)
    @Enumerated(EnumType.STRING)
    private StateCode stateCode;
    @NotBlank(message = "The station number is a required field")
    @Pattern(regexp = "^[0-9]{2}$",message = "Incorrect format. Must be a two(2) digit number: 01,02 ...")
    private String stationNo;

    public LocationId(StateCode stateCode , String stationNo) {
        this.stateCode = stateCode;
        this.stationNo = stationNo;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        try{
            return
                    this.stateCode == ((LocationId)obj).stateCode &&
                            this.stationNo.equals(((LocationId)obj).stationNo);
        }
        catch (Exception e) {return false;}
    }
}
