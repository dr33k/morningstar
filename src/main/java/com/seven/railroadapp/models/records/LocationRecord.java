package com.seven.railroadapp.models.records;

import com.seven.railroadapp.models.entities.Location;
import com.seven.railroadapp.models.entities.LocationId;
import com.seven.railroadapp.models.enums.LocationStatus;
import com.seven.railroadapp.models.enums.StateName;
import com.seven.railroadapp.models.requests.LocationCreateRequest;
import com.seven.railroadapp.models.requests.LocationUpdateRequest;


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
    public String toString(){
        return this.locationId.toString();
    }
}