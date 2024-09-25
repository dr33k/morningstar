package com.seven.morningstar.backend.voyage;

import com.seven.morningstar.backend.location.LocationId;
import com.seven.morningstar.backend.location.enums.VoyageStatus;

import java.time.ZonedDateTime;
import java.util.UUID;

public record VoyageRecord(
        UUID voyageNo,
        LocationId departureLocationId,
        LocationId arrivalLocationId,
        ZonedDateTime departureDateTime,

        ZonedDateTime arrivalDateTime,
        VoyageStatus status,
        Boolean published,
        String message
) {
    public static VoyageRecord copy(Voyage b){
        return new VoyageRecord(
                b.getVoyageNo(),
                b.getDepartureLocation().getLocationId(),
                b.getArrivalLocation().getLocationId(),
                b.getDepartureDateTime(),
                b.getArrivalDateTime(),
                b.getStatus(),
                b.getPublished(),
                null
        );
    }
    public static VoyageRecord copy(VoyageCreateRequest b){
        return new VoyageRecord(
                null,
                b.getDepartureLocationId(),
                b.getArrivalLocationId(),
                b.getDepartureDateTime(),
                null,
                null,
                false,
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
                b.getPublished(),
                null
        );
    }
    public String toString(){
        return this.voyageNo.toString();
    }
}