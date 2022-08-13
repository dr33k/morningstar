package com.seven.RailroadApp.services;

import com.seven.RailroadApp.models.entities.Location;
import com.seven.RailroadApp.models.entities.Location;
import com.seven.RailroadApp.models.entities.LocationId;
import com.seven.RailroadApp.models.records.LocationRecord;
import com.seven.RailroadApp.models.records.LocationRecord;
import com.seven.RailroadApp.models.records.UserRecord;
import com.seven.RailroadApp.repositories.LocationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class LocationService extends com.seven.RailroadApp.services.Service {
    @Autowired
    LocationRepository locationRepository;
    @Override
    Set<LocationRecord> getAll() {
        Set<LocationRecord> locationRecords = new HashSet<>(0);
        List<Location> locationList = locationRepository.findAll();
        for (Location location : locationList) {
            LocationRecord locationRecord = LocationRecord.copy(location);
            locationRecords.add(locationRecord);
        }
        return locationRecords;
    }
    @Override
    Record get(Object id) {
        try {
            LocationId locationId = (LocationId) id;
            Optional<Location> locationReturned = locationRepository.findByStateCodeAndStationNo(locationId.getStateCode(),locationId.getStationNo());
            /*If a value is present, map returns an Optional describing the result of applying
             the given mapping function to the value, otherwise returns an empty Optional.
            If the mapping function returns a null result then this method returns an empty Optional.
             */
            return locationReturned.map(LocationRecord::copy).orElse(null);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    Boolean delete(Object id) {
        try {
            LocationRecord locationRecord = (LocationRecord)get((LocationId) id);
            if(locationRecord != null) locationRepository.deleteByStateCodeAndStationNo(locationRecord.stateCode(),locationRecord.stationNo());
        } catch (Exception ex) {return false;}
        return false;
    }

    @Override
    Record create(Record recordObject) {
        try {//Cast recordObject into LocationRecord class
            LocationRecord locationRecord = (LocationRecord) recordObject;
            Location location = new Location();
            BeanUtils.copyProperties(locationRecord, location);

            //Set the State code from the StateName enum
            location.setStateCode(locationRecord.stateName().getStateCode());

            //Set the Station number from the Number of rows with the same StateCode in the database + 1
            Integer stateCodeNumber = (locationRepository.countByStateCode(location.getStateCode()) + 1);
            String stationNo = leadingZeros(stateCodeNumber.toString(),2);
            location.setStationNo(stationNo);

            //Save
            locationRepository.save(location);
            return locationRecord;
        }catch(Exception ex) {return null;}
    }

    @Override
    Record update(Record recordObject) {
        Boolean modified = false;
        try {//Retrieve indicated Location Object from the Database
            LocationRecord propertiesToUpdate = (LocationRecord) recordObject;
            Optional<Location> locationReturned = locationRepository.findByStateCodeAndStationNo(propertiesToUpdate.stateCode(),propertiesToUpdate.stationNo());

            if (locationReturned.isPresent()) {
                Location location = locationReturned.get();
                //If the property is not null and is a different value from before
                if(propertiesToUpdate.stationName()!=null && !propertiesToUpdate.stationName().equals(location.getStationName())) {
                    location.setStationName(propertiesToUpdate.stationName());
                    modified =  (modified)?modified:true;
                }
                if(propertiesToUpdate.stateName()!=null && !propertiesToUpdate.stateName().equals(location.getStateName())) {
                    location.setStateName(propertiesToUpdate.stateName());
                    modified =  (modified)?modified:true;
                }
                if(modified) {
                    locationRepository.save(location);
                    return LocationRecord.copy(location);
                }
            }
        } catch (Exception ex) {return null;}
        return null;
    }

    private String leadingZeros(String s, int reqLength){
        while(s.length() < reqLength){
            s="0"+s;
        }
        return s;
    }
}
