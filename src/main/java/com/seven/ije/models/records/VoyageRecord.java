package com.seven.ije.models.records;

import com.seven.ije.models.entities.LocationId;
import com.seven.ije.models.entities.Voyage;
import com.seven.ije.models.enums.VoyageStatus;
import com.seven.ije.models.requests.VoyageCreateRequest;
import com.seven.ije.models.requests.VoyageUpdateRequest;

import java.time.LocalDateTime;
import java.util.UUID;

public record VoyageRecord(
        UUID voyageNo,
        LocationId departureLocationId,
        LocationId arrivalLocationId,
        LocalDateTime travelDateTime,

        LocalDateTime arrivalDateTime,
        VoyageStatus status,
        String message
) {
    public static VoyageRecord copy(Voyage b){
        return new VoyageRecord(
                b.getVoyageNo(),
                b.getDepartureLocation().getLocationId(),
                b.getArrivalLocation().getLocationId(),
                b.getTravelDateTime(),
                b.getArrivalDateTime(),
                b.getStatus(),
                null
        );
    }
    public static VoyageRecord copy(VoyageCreateRequest b){
        return new VoyageRecord(
                null,
                b.getDepartureLocationId(),
                b.getArrivalLocationId(),
                b.getTravelDateTime(),
                null,
                null,
                null
        );
    }
    public static VoyageRecord copy(VoyageUpdateRequest b){
        return new VoyageRecord(
                b.getVoyageNo(),
                null,
                null,
                null,
                null,
                b.getStatus(),
                null
        );
    }
    public String toString(){
        return this.voyageNo.toString();
    }
}