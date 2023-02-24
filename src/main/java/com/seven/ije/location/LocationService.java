package com.seven.ije.location;

import com.seven.ije.enums.LocationStatus;
import com.seven.ije.AppRequest;
import com.seven.ije.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

import static com.seven.ije.enums.LocationStatus.*;

@Service("locationService")
@Transactional
public class LocationService implements AppService <LocationRecord, AppRequest> {
    @Autowired
    LocationRepository locationRepository;

    @Override
    public Set <LocationRecord> getAll() {
        List <Location> locationList = locationRepository.findAll();

        Set <LocationRecord> locationRecords = locationList.stream().map(LocationRecord::copy).collect(Collectors.toSet());

        return locationRecords;
    }

    @Override
    public LocationRecord get(Object id) {
        LocationId locationId = (LocationId) id;
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,
                        "This location"+locationId.getStateCode()+"_"+locationId.getStationNo()+"does not exist or has been deleted"));
        return LocationRecord.copy(location);
    }

    public LocationRecord getAvailable(Object id) {
        LocationId locationId = (LocationId) id;
        Location location = locationRepository.findByLocationIdAndStatusIn(locationId, List.of(USED,UNUSED))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,
                        "This location"+locationId.getStateCode()+"_"+locationId.getStationNo()+"is disabled or has been deleted"));
        return LocationRecord.copy(location);
    }
    @Override
    public void delete(Object id) {
        if (locationRepository.deleteByLocationIdAndStatus((LocationId) id , UNUSED) == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Delete could not be performed. Why?:" +
                    " \n 1) Locations that have been USED cannot be deleted but can be DISABLED. This is to preserve past records" +
                    "\n2) This location has already been deleted");
        }
    }

    @Override
    public LocationRecord create(AppRequest request) {
        try {//Cast recordObject into LocationRecord class
            LocationCreateRequest locationCreateRequest = (LocationCreateRequest) request;

            //Create Location entity
            Location location = Location.of(locationCreateRequest);

            //Create a LocationId object
            LocationId locationId = new LocationId();

            //Set the State code from the StateCode.java enum
            locationId.setStateCode(locationCreateRequest.getStateCode());

            //Set the Station number from the Number of rows with the same StateCode in the database + 1
            Integer stateCodeNumber = (locationRepository.countByLocationIdStateCode(locationId.getStateCode().name()) + 1);
            String stationNo = leadingZeros(stateCodeNumber.toString() , 2);
            locationId.setStationNo(stationNo);

            //Set LocationId object
            location.setLocationId(locationId);

            //Set State Name
            location.setStateName(locationCreateRequest.getStateCode().getStateName());

            //Set Location status
            location.setStatus(UNUSED);

            //Save
            locationRepository.save(location);
            return LocationRecord.copy(location);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR ,
                    "Location could be created, please contact System Administrator. Why? " + ex.getMessage());
        }
    }

    @Override
    public LocationRecord update(AppRequest request) {

        try {
            //Retrieve indicated Location Object from the Database
            LocationUpdateRequest locationUpdateRequest = (LocationUpdateRequest) request;
            Location location = locationRepository.findById(locationUpdateRequest.getLocationId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,
                    "This location does not exist or has been deleted"));

            String oldStationName = location.getStationName();
            String newStationName = locationUpdateRequest.getStationName();
            LocationStatus oldStatus = location.getStatus();
            LocationStatus newStatus = locationUpdateRequest.getStatus();

            //Update Station name
            //If the property is not null and is a different value from before
            if (newStationName != null) {
                if (newStationName != oldStationName) {
                    location.setStationName(newStationName);
                    modifyLocation(location, newStatus);
                }
            }
            else if (newStatus != null) {
                final String UNSUPPORTED = "UNSUPPORTED OPERATION. Cannot be updated to anything but %s";
                switch (oldStatus) {
                    case USED -> {
                        if (!newStatus.equals(DISABLED_USED))
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , String.format(UNSUPPORTED , DISABLED_USED));
                        modifyLocation(location , newStatus);
                    }
                    case DISABLED_USED -> {
                        if (!newStatus.equals(USED))
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , String.format(UNSUPPORTED , USED));
                        modifyLocation(location , newStatus);
                    }
                    case DISABLED_UNUSED -> {
                        if (!newStatus.equals(UNUSED))
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , String.format(UNSUPPORTED , UNUSED));
                        modifyLocation(location , newStatus);
                    }
                    case UNUSED -> {
                        if (newStatus.equals(DISABLED_USED))
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST ,"UNSUPPORTED OPERATION. Use DISABLED_UNUSED instead");
                        modifyLocation(location , newStatus);
                    }
                }
            }
            return LocationRecord.copy(location);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR ,
                    "Location could not be modified, please try again. Why? " + ex.getMessage() , ex);
        }
    }

    private String leadingZeros(String s , int reqLength) {
        return "0".repeat(reqLength - s.length()).concat(s);
    }

    public void modifyLocation(Location location, LocationStatus newStatus){
        location.setStatus(newStatus);
        locationRepository.save(location);
    }
}
