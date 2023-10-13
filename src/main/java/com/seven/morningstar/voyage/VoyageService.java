package com.seven.morningstar.voyage;

import com.seven.morningstar.location.Location;
import com.seven.morningstar.enums.VoyageStatus;
import com.seven.morningstar.AppRequest;
import com.seven.morningstar.AppService;
import com.seven.morningstar.location.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.seven.morningstar.enums.VoyageStatus.*;
import static com.seven.morningstar.enums.LocationStatus.*;

@Service("voyageService")
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

    public VoyageRecord getPublished(Object id) {
        Voyage voyage = voyageRepository.findByVoyageNoAndPublished((UUID) id, true).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,
                        "This voyage does is not available for reservations or has been removed"));
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
            locationService.modifyLocation(l1 , USED, null);
            locationService.modifyLocation(l2, USED, null);

            //Set voyage status
            voyage.setStatus(VoyageStatus.PENDING);
            //Set Departure date time
            voyage.setDepartureDateTime(voyageCreateRequest.getDepartureDateTime());
            //Set published status
            voyage.setPublished(false);
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
        if (voyageRepository.deleteByVoyageNoAndStatusAndPublished((UUID) id , PENDING, false) == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Voyage could not be deleted. Why:" +
                    "1) Only voyaged with the PENDING status can be deleted. Others have to be CANCELLED" +
                    "2) If the Voyage is still PUBLISHED please unpublish it first" +
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
            ZonedDateTime oldTime = voyage.getDepartureDateTime();
            ZonedDateTime newTime = voyageUpdateRequest.getTravelDateTime();
            Set <VoyageStatus> unupdatable = Set.of(CANCELLED , COMPLETED);

            if(voyageUpdateRequest.getPublished() != null){
                voyage.setPublished(voyageUpdateRequest.getPublished());
                modified = true;
            }
            else if (newStatus != null) {
                if (unupdatable.contains(oldStatus))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Updating a CANCELLED or COMPLETED voyage is not permitted");
                if (newStatus.equals(PENDING))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Voyages can only be PENDING once");
                voyage.setStatus(newStatus);
                modified = true;
            }
            else if (newTime != null) {
                if (newTime.isBefore(oldTime)) //Preponing
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Please set a date in the future");
                voyage.setDepartureDateTime(newTime);
                modified = true;
            }
            if (modified) {
                voyageRepository.save(voyage);
            }
            return VoyageRecord.copy(voyage);
        }catch (ResponseStatusException ex) {throw ex;}
        catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR ,
                    "Voyage could not be updated, please contact System Administrator. Why? " + ex.getMessage());
        }
    }
}