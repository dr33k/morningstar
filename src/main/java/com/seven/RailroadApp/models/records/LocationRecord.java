package com.seven.RailroadApp.models.records;

import com.seven.RailroadApp.models.entities.Location;
import com.seven.RailroadApp.models.enums.StateName;

public record LocationRecord(
        String stateCode,
        String stationNo,
        String stationName,
        StateName stateName
) {
    public static LocationRecord copy(Location l){
       return new LocationRecord(
               l.getStateCode(),
               l.getStationNo(),
               l.getStationName(),
               l.getStateName()
       );
    }
}