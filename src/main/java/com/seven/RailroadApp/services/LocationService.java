package com.seven.RailroadApp.services;

import com.seven.RailroadApp.models.entities.Location;
import com.seven.RailroadApp.models.entities.LocationId;
import com.seven.RailroadApp.models.records.LocationRecord;
import com.seven.RailroadApp.repositories.LocationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import static com.seven.RailroadApp.models.enums.LocationStatus.*;
@Service
@Transactional
public class LocationService implements com.seven.RailroadApp.services.Service {
    @Autowired
    LocationRepository locationRepository;
    @Override
    public Set<LocationRecord> getAll() {
        Set<LocationRecord> locationRecords = new HashSet<>(0);
        List<Location> locationList = locationRepository.findAll();
        for (Location location : locationList) {
            LocationRecord locationRecord = LocationRecord.copy(location);
            locationRecords.add(locationRecord);
        }
        return locationRecords;
    }
    @Override
    public Record get(Object id) {
        try {
            LocationId locationId = (LocationId) id;
            Optional<Location> locationReturned = locationRepository.findById(locationId);
            /*If a value is present, map returns an Optional describing the result of applying
             the given mapping function to the value, otherwise returns an empty Optional.
            If the mapping function returns a null result then this method returns an empty Optional.
             */
            return locationReturned.map(LocationRecord::copy).orElse(null);
        } catch (Exception ex) {
            return new LocationRecord(null, null, null,null,
                    "Location not found, please make sure search credentials are entered properly. Possibly: "+ex.getMessage()
            );
        }
    }
    @Override
    public Boolean delete(Object id) {
        try {
            LocationId locationId = (LocationId) id;
            Optional<Location> lOpt = locationRepository.findById(locationId);
            if(lOpt.isPresent()) {
                if (lOpt.get().getStatus().equals(UNUSED)) {
                    locationRepository.deleteById(locationId);
                    return true;
                }
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }
    @Override
    public Record create(Record recordObject) {
        try {//Cast recordObject into LocationRecord class
            LocationRecord locationRecord = (LocationRecord) recordObject;

                //Create Location entity
                Location location = new Location();
                BeanUtils.copyProperties(locationRecord, location);

                //Create a LocationId object
                LocationId locationId = new LocationId();

                //Set the State code from the StateName enum
                locationId.setStateCode(locationRecord.stateName().getStateCode());

                //Set the Station number from the Number of rows with the same StateCode in the database + 1
                Integer stateCodeNumber = (locationRepository.countByLocationIdStateCode(locationId.getStateCode()) + 1);
                String stationNo = leadingZeros(stateCodeNumber.toString(), 2);
                locationId.setStationNo(stationNo);

                //Set LocationId object
                location.setLocationId(locationId);

                //Set Location status
                location.setStatus(UNUSED);

                //Save
                locationRepository.save(location);
                return LocationRecord.copy(location);
        }catch(Exception ex) {
            return new LocationRecord(null, null, null,null,
                    "Location could be created, please try again later. Why? "+ex.getMessage()
            );
        }
    }

    @Override
    public Record update(Record recordObject) {
        boolean modified = false;
        try {//Retrieve indicated Location Object from the Database
            LocationRecord propertiesToUpdate = (LocationRecord) recordObject;
            Optional<Location> locationReturned = locationRepository.findById(propertiesToUpdate.locationId());

            if (locationReturned.isPresent()) {
                Location location = locationReturned.get();
                //If the property is not null and is a different value from before
                if(propertiesToUpdate.stationName()!=null && !propertiesToUpdate.stationName().equals(location.getStationName())) {
                    location.setStationName(propertiesToUpdate.stationName());
                    modified =  (modified)?modified:true;
                }
                if(propertiesToUpdate.status() != null && propertiesToUpdate.status().equals(INACTIVE)&& location.getStatus().equals(USED)){
                    location.setStatus(INACTIVE);
                    modified =  (modified)?modified:true;
                }
                if(modified) {
                    ;;;
                    locationRepository.save(location);
                    return LocationRecord.copy(location);
                }
            }
        } catch (Exception ex) {
            return new LocationRecord(null, null, null,null,
                    "Location could not be modified, please try again. Why? "+ex.getMessage()
            );
        }
        return null;
    }

    private String leadingZeros(String s, int reqLength){
        while(s.length() < reqLength){
            s="0"+s;
        }
        return s;
    }
}
