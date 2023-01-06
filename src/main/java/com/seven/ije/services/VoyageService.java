package com.seven.ije.services;

import com.seven.ije.models.entities.Booking;
import com.seven.ije.models.entities.Voyage;
import com.seven.ije.models.entities.Location;
import com.seven.ije.models.enums.LocationStatus;
import com.seven.ije.models.enums.VoyageStatus;
import com.seven.ije.models.records.BookingRecord;
import com.seven.ije.models.records.LocationRecord;
import com.seven.ije.models.records.VoyageRecord;
import com.seven.ije.models.requests.AppRequest;
import com.seven.ije.models.requests.VoyageCreateRequest;
import com.seven.ije.models.requests.VoyageUpdateRequest;
import com.seven.ije.repositories.VoyageRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.seven.ije.models.enums.VoyageStatus.*;
import static com.seven.ije.models.enums.LocationStatus.*;

@Service
@Transactional
public class VoyageService implements AppService <VoyageRecord, AppRequest> {
    private VoyageRepository voyageRepository;
    private LocationService locationService;

    public VoyageService(VoyageRepository voyageRepository , LocationService locationService) {
        this.voyageRepository = voyageRepository;
        this.locationService = locationService;
    }

    @Override
    public Set <VoyageRecord> getAll() {
        List <Voyage> voyageList = voyageRepository.findAll();

        Set <VoyageRecord> voyageRecords = voyageList.stream().map(VoyageRecord::copy).collect(Collectors.toSet());

        return voyageRecords;
    }

    @Override
    public VoyageRecord get(Object id) {
        Voyage voyage = voyageRepository.findById((UUID) id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,
                        "This voyage does not exist or has been removed"));
        return VoyageRecord.copy(voyage);
    }

    @Override
    public VoyageRecord create(AppRequest request) {
        try {
            VoyageCreateRequest voyageCreateRequest = (VoyageCreateRequest) request;

            Voyage voyage = new Voyage();

            //Set Arrival Location
            Location l1 = Location.of(locationService.getAvailable(voyageCreateRequest.getArrivalLocationId()));
            voyage.setArrivalLocation(l1);

            //Set Departure Location
            Location l2 = Location.of(locationService.getAvailable(voyageCreateRequest.getDepartureLocationId()));
            voyage.setDepartureLocation(l2);

            //Update locations
            locationService.modifyLocation(l1 , USED);
            locationService.modifyLocation(l2, USED);

            //Set voyage status
            voyage.setStatus(VoyageStatus.PENDING);

            //Save
            voyageRepository.save(voyage);

            return VoyageRecord.copy(voyage);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Voyage could not be created. Why? " + ex.getMessage(), ex);
        }
    }

    @Override
    public void delete(Object id) {
        if (voyageRepository.deleteByIdAndStatus((UUID) id , PENDING.name()) == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Voyage could not be deleted. Why:" +
                    "1) Only voyaged with the PENDING status can be deleted. Others have to be CANCELLED" +
                    "2) The voyage selected does not exist or has been removed");
    }

    @Override
    public VoyageRecord update(AppRequest request) {
        Boolean modified = false;
        try {//Retrieve indicated Voyage Object from the Database
            VoyageUpdateRequest voyageUpdateRequest = (VoyageUpdateRequest) request;
            Voyage voyage = voyageRepository.findById(voyageUpdateRequest.getVoyageNo())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND , "Voyage does not exist"));

            VoyageStatus oldStatus = voyage.getStatus();
            VoyageStatus newStatus = voyageUpdateRequest.getStatus();
            LocalDateTime oldTime = voyage.getTravelDateTime();
            LocalDateTime newTime = voyageUpdateRequest.getTravelDateTime();
            Set <VoyageStatus> unupdatable = Set.of(CANCELLED , COMPLETED);

            if (newStatus != null) {
                if (unupdatable.contains(oldStatus))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Updating a CANCELLED or COMPLETED voyage is not permitted");
                if (newStatus.equals(PENDING))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Voyages can only be PENDING once");
                voyage.setStatus(newStatus);
                modified = true;
            }
            if (newTime != null) {
                if (newTime.isBefore(oldTime)) //Preponing
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Please set a date in the future");
                voyage.setTravelDateTime(newTime);
                modified = true;
            }
            if (modified) {
                voyageRepository.save(voyage);
            }
            return VoyageRecord.copy(voyage);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR ,
                    "Voyage could not be updated, please contact System Administrator. Why? " + ex.getMessage());
        }
    }
}