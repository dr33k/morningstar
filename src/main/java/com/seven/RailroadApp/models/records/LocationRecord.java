package com.seven.RailroadApp.models.records;

import com.seven.RailroadApp.models.entities.Location;
import com.seven.RailroadApp.models.entities.Ticket;
import com.seven.RailroadApp.models.enums.StateCode;
import org.springframework.beans.BeanUtils;

public record LocationRecord(
        StateCode stateCode,
        String stationNo,
        String stationName,
        String stationLocation
) {
    public static LocationRecord copy(Location l){
       return new LocationRecord(
               l.getStateCode(),
               l.getStationNo(),
               l.getStationName(),
               l.getStationLocation()
       );
    }
}