package com.seven.RailroadApp.models.records;

import com.seven.RailroadApp.models.entities.*;
import com.seven.RailroadApp.models.enums.VoyageStatus;
import com.seven.RailroadApp.models.requests.VoyageCreateRequest;
import com.seven.RailroadApp.models.requests.VoyageUpdateRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record VoyageRecord(
        UUID voyageNo,
        LocationId departureLocationId,
        LocationId arrivalLocationId,
        LocalDate travelDate,
        LocalTime travelTime,
        LocalDateTime arrivalDateTime,
        VoyageStatus status,
        String message
) {
    public static VoyageRecord copy(Voyage b){
        return new VoyageRecord(
                b.getVoyageNo(),
                b.getDepartureLocation().getLocationId(),
                b.getArrivalLocation().getLocationId(),
                b.getTravelDate(),
                b.getTravelTime(),
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
                b.getTravelDate(),
                b.getTravelTime(),
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
                b.getArrivalDateTime(),
                b.getStatus(),
                null
        );
    }
}