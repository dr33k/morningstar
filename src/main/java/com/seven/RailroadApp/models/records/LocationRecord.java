package com.seven.RailroadApp.models.records;

import com.seven.RailroadApp.models.entities.Location;
import com.seven.RailroadApp.models.entities.LocationId;
import com.seven.RailroadApp.models.enums.LocationStatus;
import com.seven.RailroadApp.models.enums.StateName;
import com.seven.RailroadApp.models.requests.LocationCreateRequest;
import com.seven.RailroadApp.models.requests.LocationUpdateRequest;

public record LocationRecord(
        LocationId locationId,
        String stationName,
        StateName stateName,
        LocationStatus status,
        String message
) {
    public static LocationRecord copy(Location l){
       return new LocationRecord(
               l.getLocationId(),
               l.getStationName(),
               l.getStateName(),
               l.getStatus(),
               null
       );
    }
    public static LocationRecord copy(LocationCreateRequest lr){
        return new LocationRecord(
                null,
                lr.getStationName(),
                lr.getStateName(),
                null,
                null
        );
    }
    public static LocationRecord copy(LocationUpdateRequest lr){
        return new LocationRecord(
                lr.getLocationId(),
                lr.getStationName(),
                null,
                lr.getStatus(),
                null
        );
    }
}