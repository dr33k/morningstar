package com.seven.RailroadApp.models.entities;

import lombok.Data;

import java.io.Serializable;
//Composite primary Key class
@Data
public class LocationId implements Serializable {

    private String stateCode;
    private String stationNo;

    public LocationId(String stateCode, String stationNo) {
        this.stateCode = stateCode;
        this.stationNo = stationNo;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
