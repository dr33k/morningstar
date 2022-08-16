package com.seven.RailroadApp.services;

import com.seven.RailroadApp.models.entities.Voyage;
import com.seven.RailroadApp.models.entities.Location;
import com.seven.RailroadApp.models.entities.User;
import com.seven.RailroadApp.models.enums.VoyageStatus;
import com.seven.RailroadApp.models.records.VoyageRecord;
import com.seven.RailroadApp.models.records.LocationRecord;
import com.seven.RailroadApp.models.records.TicketRecord;
import com.seven.RailroadApp.models.records.UserRecord;
import com.seven.RailroadApp.repositories.VoyageRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@Transactional
public class VoyageService implements com.seven.RailroadApp.services.Service {
    @Autowired
    private VoyageRepository voyageRepository;
    @Autowired
    private TicketService ticketService;

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
            LocationRecord lr = (LocationRecord) locationService.get(voyageRecord.arrivalLocationId());

            if (lr != null && lr.message() == null) {
                Location l1 = new Location();
                BeanUtils.copyProperties(lr, l1);
                voyage.setArrivalLocation(l1);

                //Set Departure Location
                lr = (LocationRecord) locationService.get(voyageRecord.departureLocationId());
                if (lr != null && lr.message() == null) {
                    Location l2 = new Location();
                    BeanUtils.copyProperties(lr, l2);
                    voyage.setDepartureLocation(l2);

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
            return new UserRecord(null, null, null, null, null, null, null, null,
                    "Reservation could be created, please try again later. Why? " + ex.getMessage());
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
                String status = voyage.getStatus().name();
                LocalTime travelTime = voyage.getTravelTime();
                LocalDate travelDate = voyage.getTravelDate();

                if (propertiesToUpdate.travelDate().isAfter(travelDate) && !status.equals("COMPLETED") && !status.equals("IN_TRANSIT")) {
                    voyage.setTravelDate(propertiesToUpdate.travelDate());
                    modified = true;
                }
                else if (propertiesToUpdate.travelTime().isAfter(travelTime) && !status.equals("COMPLETED") && !status.equals("IN_TRANSIT")) {
                    voyage.setTravelTime(propertiesToUpdate.travelTime());
                    modified = true;
                }
                else if (propertiesToUpdate.status().equals("IN_TRANSIT") && !propertiesToUpdate.status().name().equals(status) && status.equals("PENDING")) {
                    voyage.setStatus(VoyageStatus.IN_TRANSIT);
                    modified = true;
                } else if (propertiesToUpdate.status().equals("COMPLETED") && !propertiesToUpdate.status().name().equals(status)&& status.equals("IN_TRANSIT")) {
                    voyage.setStatus(VoyageStatus.COMPLETED);
                    voyage.setArrivalDateTime(LocalDateTime.now());
                    modified = true;
                } else if (propertiesToUpdate.status().equals("CANCELLED") && !propertiesToUpdate.status().name().equals(status) && !status.equals("COMPLETED")) {
                    voyage.setStatus(VoyageStatus.CANCELLED);
                    modified = true;
                } else if (propertiesToUpdate.status().equals("PENDING") && !propertiesToUpdate.status().name().equals(status) && status.equals("IN_TRANSIT")) {
                    voyage.setStatus(VoyageStatus.PENDING);
                    modified = true;
                }
                if (modified) {
                    voyageRepository.save(voyage);
                    return VoyageRecord.copy(voyage);
                }
                else{return new UserRecord(null, null, null, null, null, null, null, null,
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
            return new UserRecord(null, null, null, null, null, null, null, null,
                    "Voyage Record could be updated, please try again later. Why? " + ex.getMessage());
        }
        return null;
    }
}