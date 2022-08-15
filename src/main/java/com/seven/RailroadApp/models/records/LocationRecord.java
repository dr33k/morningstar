package com.seven.RailroadApp.models.records;

import com.seven.RailroadApp.models.entities.Location;
import com.seven.RailroadApp.models.entities.LocationId;
import com.seven.RailroadApp.models.enums.StateName;
import com.seven.RailroadApp.models.requests.LocationRequest;

public record LocationRecord(
        LocationId locationId,
        String stationName,
        StateName stateName,
        String message
) {
    public static LocationRecord copy(Location l){
       return new LocationRecord(
               l.getLocationId(),
               l.getStationName(),
               l.getStateName(),
               null
       );
    }
    public static LocationRecord copy(LocationRequest lr){
        return new LocationRecord(
                lr.getLocationId(),
                lr.getStationName(),
                lr.getStateName(),
                null
        );
    }
}