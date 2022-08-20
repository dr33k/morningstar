package com.seven.RailroadApp.services;

import com.seven.RailroadApp.models.entities.Voyage;
import com.seven.RailroadApp.models.entities.Location;
import com.seven.RailroadApp.models.entities.User;
import com.seven.RailroadApp.models.enums.LocationStatus;
import com.seven.RailroadApp.models.enums.VoyageStatus;
import com.seven.RailroadApp.models.records.VoyageRecord;
import com.seven.RailroadApp.models.records.LocationRecord;
import com.seven.RailroadApp.models.records.TicketRecord;
import com.seven.RailroadApp.models.records.UserRecord;
import com.seven.RailroadApp.repositories.LocationRepository;
import com.seven.RailroadApp.repositories.VoyageRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import static com.seven.RailroadApp.models.enums.VoyageStatus.*;

@Service
@Transactional
public class VoyageService implements com.seven.RailroadApp.services.Service {
    @Autowired
    private VoyageRepository voyageRepository;

    @Autowired
    private LocationRepository locationReposistory;

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
            return null;
        }
    }

    @Override
    public Record create(Record recordObject) {
        try {
            VoyageRecord voyageRecord = (VoyageRecord) recordObject;

            Voyage voyage = new Voyage();
            BeanUtils.copyProperties(voyageRecord, voyage);

            //Set Arrival Location
            Optional<Location> l1Opt = locationReposistory.findById(voyageRecord.arrivalLocationId());

            if (l1Opt.isPresent()) {
                Location l1 = l1Opt.get();
                voyage.setArrivalLocation(l1);

                //Set Departure Location
                Optional<Location> l2Opt = locationReposistory.findById(voyageRecord.departureLocationId());
                if (l2Opt.isPresent()) {
                    Location l2 = l1Opt.get();
                    voyage.setDepartureLocation(l2);

                    //Set voyage status
                    voyage.setStatus(VoyageStatus.PENDING);
                    //Set voyage no
                    voyage.setVoyageNo(UUID.randomUUID());

                    //Update locations
                    l1.setStatus(LocationStatus.USED);
                    l2.setStatus(LocationStatus.USED);
                    locationReposistory.save(l1);
                    locationReposistory.save(l2);
                    //Save
                    voyageRepository.save(voyage);

                    return VoyageRecord.copy(voyage);
                }
            }
        } catch (Exception ex) {
            return new VoyageRecord(null, null, null,  null, null, null,
                    "Voyage Record could be created, please try again later. Why? " + ex.getMessage());
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
                LocalDateTime travelDate = voyage.getTravelDate();

                if (propertiesToUpdate.travelDate() != null && propertiesToUpdate.travelDate().isAfter(travelDate) && status.equals(PENDING)) {
                    voyage.setTravelDate(propertiesToUpdate.travelDate());
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
            return new VoyageRecord( null, null, null, null, null, null,
                    "Voyage Record could be updated, please try again later. Why? " + ex.getMessage());
        }
        return null;
    }
}