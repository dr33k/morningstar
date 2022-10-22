package com.seven.ije.services;

import com.seven.ije.models.entities.Voyage;
import com.seven.ije.models.entities.Location;
import com.seven.ije.models.enums.LocationStatus;
import com.seven.ije.models.enums.VoyageStatus;
import com.seven.ije.models.records.LocationRecord;
import com.seven.ije.models.records.VoyageRecord;
import com.seven.ije.repositories.VoyageRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import static com.seven.ije.models.enums.VoyageStatus.*;
import static com.seven.ije.models.enums.LocationStatus.*;

@Service
@Transactional
public class VoyageService implements com.seven.ije.services.Service {
    @Autowired
    private VoyageRepository voyageRepository;

    @Autowired
    private LocationService locationService;

    @Override
    public Set<VoyageRecord> getAll() {
        Set<VoyageRecord> voyageRecords = new HashSet<>(0);
        List<Voyage> voyageList = voyageRepository.findAll();
        for (Voyage voyage : voyageList) {
            VoyageRecord voyageRecord = VoyageRecord.copy(voyage);
            voyageRecords.add(voyageRecord);
        }
        return voyageRecords;
    }

    @Override
    public Record get(Object id) {
        try {
            Optional<Voyage> voyageReturned = voyageRepository.findById((UUID) id);
            /*If a value is present, map returns an Optional describing the result of applying
             the given mapping function to the value, otherwise returns an empty Optional.
            If the mapping function returns a null result then this method returns an empty Optional.
             */
            return voyageReturned.map(VoyageRecord::copy).orElse(null);
        } catch (Exception ex) {
            throw new RuntimeException("Voyage not found, please make sure details are entered correctly");
        }
    }

    public Voyage getVoyageEntity(Object id) {
        try {
            Optional<Voyage> voyageReturned = voyageRepository.findById((UUID) id);
            return voyageReturned.orElse(null);
        } catch (Exception ex) {
            throw new RuntimeException("Voyage not found, please make sure details are entered correctly");
        }
    }
    @Override
    public Record create(Record recordObject) {
        try {
            VoyageRecord voyageRecord = (VoyageRecord) recordObject;

            Voyage voyage = new Voyage();
            BeanUtils.copyProperties(voyageRecord, voyage);

            //Set Arrival Location
            Location l1 =locationService.getLocationEntity(voyageRecord.arrivalLocationId());

            if (l1 != null) {
                voyage.setArrivalLocation(l1);

                //Set Departure Location
                Location l2 = locationService.getLocationEntity(voyageRecord.departureLocationId());
                if (l2 != null) {
                    voyage.setDepartureLocation(l2);

                    //Update locations
                   if(l1.getStatus().equals(UNUSED))
                       updateLocation(l1, USED);
                   if (l2.getStatus().equals(UNUSED))
                       updateLocation(l2,USED);
                   else if(l1.getStatus().equals(INACTIVE) || l2.getStatus().equals(INACTIVE))
                       throw new RuntimeException("One or both of the locations provided are INACTIVE");


                    //Set voyage status
                    voyage.setStatus(VoyageStatus.PENDING);
                    //Set voyage no
                    voyage.setVoyageNo(UUID.randomUUID());
                   //Save
                    voyageRepository.save(voyage);

                    return VoyageRecord.copy(voyage);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Voyage could not be created. Why? "+ex.getMessage());
        }
        return null;
    }

    @Override
    public Boolean delete(Object id) {
        return false;
    }

    @Override
    public Record update(Record recordObject) {
        Boolean modified = false;
        try {//Retrieve indicated Voyage Object from the Database
            VoyageRecord propertiesToUpdate = (VoyageRecord) recordObject;
            Optional<Voyage> voyageReturned = voyageRepository.findById(propertiesToUpdate.voyageNo());

            if (voyageReturned.isPresent()) {
                Voyage voyage = voyageReturned.get();
                VoyageStatus status = voyage.getStatus();
                LocalDateTime travelDateTime = voyage.getTravelDateTime();

                if (propertiesToUpdate.travelDateTime() != null && propertiesToUpdate.travelDateTime().isAfter(travelDateTime) && status.equals(PENDING)) {
                    voyage.setTravelDateTime(propertiesToUpdate.travelDateTime());
                    modified = true;
                }
                else if (propertiesToUpdate.status()!= null && propertiesToUpdate.status().equals(IN_TRANSIT) && !propertiesToUpdate.status().equals(status) && status.equals(PENDING)) {
                    voyage.setStatus(IN_TRANSIT);
                    modified = true;
                } else if (propertiesToUpdate.status()!= null && propertiesToUpdate.status().equals(COMPLETED) && !propertiesToUpdate.status().equals(status)&& status.equals(IN_TRANSIT)) {
                    voyage.setStatus(VoyageStatus.COMPLETED);
                    voyage.setArrivalDateTime(LocalDateTime.now());
                    modified = true;
                } else if (propertiesToUpdate.status()!= null && propertiesToUpdate.status().equals(CANCELLED) && !propertiesToUpdate.status().equals(status) && !status.equals(COMPLETED)) {
                    voyage.setStatus(VoyageStatus.CANCELLED);
                    modified = true;
                } else if (propertiesToUpdate.status()!= null && propertiesToUpdate.status().equals(PENDING) && !propertiesToUpdate.status().equals(status) && status.equals(IN_TRANSIT)) {
                    voyage.setStatus(VoyageStatus.PENDING);
                    modified = true;
                }
                if (modified) {
                    voyageRepository.save(voyage);
                    return VoyageRecord.copy(voyage);
                }
                else{return new VoyageRecord( null, null, null, null, null, null,
                        "Voyage Record could be updated, Why? " +
                                "* Please postpone travel date and time (if required) to values in the future" +
                                "* IN_TRANSIT updates can only be processed when the voyage is PENDING" +
                                "* COMPLETED updates can only be processed when the voyage is IN_TRANSIT" +
                                "* CANCELLED updates can only be processed if the voyage is not COMPLETED yet" +
                                "* PENDING updates can only be processed when the voyage is IN_TRANSIT" +
                                "* Finally, please dont update with the same information as before.");
                }
            }
        } catch (Exception ex) {
           throw new RuntimeException("Voyage Record could be updated, please try again later. Why? " + ex.getMessage());
        }
        return null;
    }

    private void updateLocation(Location l, LocationStatus status){
        l.setStatus(status);
        LocationRecord lr = LocationRecord.copy(l);
        locationService.update(lr);
    }
}