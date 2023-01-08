package com.seven.ije.models.records;

import com.seven.ije.models.entities.Location;
import com.seven.ije.models.entities.LocationId;
import com.seven.ije.models.enums.LocationStatus;
import com.seven.ije.models.requests.LocationCreateRequest;
import com.seven.ije.models.requests.LocationUpdateRequest;

public record LocationRecord(
        LocationId locationId,
        String stationName,
        String stateName ,
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
                lr.getStateCode().toString(),
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