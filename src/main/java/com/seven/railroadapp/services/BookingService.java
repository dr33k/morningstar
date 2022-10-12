package com.seven.railroadapp.services;

import com.seven.railroadapp.config.security.UserAuthentication;
import com.seven.railroadapp.models.entities.*;
import com.seven.railroadapp.models.enums.BookingStatus;
import com.seven.railroadapp.models.enums.VoyageStatus;
import com.seven.railroadapp.models.records.BookingRecord;
import com.seven.railroadapp.repositories.BookingRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import static com.seven.railroadapp.models.enums.BookingStatus.*;
@Service
@Transactional
public class BookingService implements com.seven.railroadapp.services.Service {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private VoyageService voyageService;
    @Autowired
    private UserAuthentication userAuthentication;

    @Override
    public Set<BookingRecord> getAll() {
        Set<BookingRecord> bookingRecords = new HashSet<>(0);
        List<Booking> bookingList = bookingRepository.findAll();
        for (Booking booking : bookingList) {
            BookingRecord bookingRecord = BookingRecord.copy(booking);
            bookingRecords.add(bookingRecord);
        }
        return bookingRecords;
    }

    @Override
    public Record get(Object id) {
        try {
            Optional<Booking> bookingReturned = bookingRepository.findById((UUID) id);
            /*If a value is present, map returns an Optional describing the result of applying
             the given mapping function to the value, otherwise returns an empty Optional.
            If the mapping function returns a null result then this method returns an empty Optional.
             */
            return bookingReturned.map(BookingRecord::copy).orElse(null);
        } catch (Exception ex) {
            throw new RuntimeException("Booking not found, please check the details entered and try again");
        }
    }

    public Booking getBookingEntity(Object id) {
        try {
            Optional<Booking> bookingReturned = bookingRepository.findById((UUID) id);
            return bookingReturned.orElse(null);
        } catch (Exception ex) {
            throw new RuntimeException("Booking not found, please check the details entered and try again");
        }
    }

    public Set<BookingRecord> getAllByPassenger(String email){
        Set<BookingRecord> bookingRecords = new HashSet<>(0);
        List<Booking> bookingList = bookingRepository.findAllByPassengerEmail(email);
        for (Booking booking : bookingList) {
            BookingRecord bookingRecord = BookingRecord.copy(booking);
            bookingRecords.add(bookingRecord);
        }
        return bookingRecords;
    }

    @Override
    public Record create(Record recordObject) {
        try {
            BookingRecord bookingRecord = (BookingRecord) recordObject;

            Booking booking = new Booking();
            BeanUtils.copyProperties(bookingRecord, booking);

            Voyage voyage = voyageService.getVoyageEntity(bookingRecord.voyageNo());

            if (voyage != null) {
                if(!voyage.getStatus().equals(VoyageStatus.PENDING)) throw new RuntimeException("This voyage has already begun or has been completed. It cannot be reserved.");
                //Set Voyage
                booking.setVoyage(voyage);
                //Set Passenger
                User user = (User) userAuthentication.getInstance().getPrincipal();
                booking.setPassenger(user);
                //Set booking status
                booking.setStatus(BookingStatus.VALID);
                //Set booking no
                booking.setBookingNo(UUID.randomUUID());
                //Set payment status
                booking.setIsPaid(false);
                //Set booking date
                booking.setBookingDateTime(LocalDateTime.now());
                //Save
                bookingRepository.save(booking);

                return BookingRecord.copy(booking);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Reservation could be created, please try again later. Why? " + ex.getMessage());
        }
        return null;
    }

    @Override
    public Boolean delete(Object id) {
        try {
            UUID bookingNo = (UUID) id;
            Optional<Booking> bOpt = bookingRepository.findById(bookingNo);
            if (bOpt.isPresent()) {
                Booking booking = bOpt.get();
                if (booking.getStatus().name().equals("CANCELLED")) {
                    bookingRepository.deleteById(bookingNo);
                    return true;
                }
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }

    @Override
    public Record update(Record recordObject) {
        boolean modified = false;
        try {//Retrieve indicated Booking Object from the Database
            BookingRecord propertiesToUpdate = (BookingRecord) recordObject;
            Optional<Booking> bookingReturned = bookingRepository.findById(propertiesToUpdate.bookingNo());

            if (bookingReturned.isPresent()) {
                Booking booking = bookingReturned.get();
                BookingStatus status = booking.getStatus();

                if (propertiesToUpdate.status().equals(CANCELLED) && !propertiesToUpdate.status().equals(status) ) {
                      booking.setStatus(CANCELLED);
                      modified = true;
                }
                if (modified) {
                    bookingRepository.save(booking);
                    return BookingRecord.copy(booking);
                } else {
                    return new BookingRecord(null, null, null, null,  null,null,null,
                            "Reservation: " + booking.getBookingNo() + " could be updated, please check your values" +
                                    " and dont update with the same information as before");
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Booking Record could not be updated, please try again later. Why? " + ex.getMessage());
        }
        return null;
    }
    public Record updateUserBookingForAdmin(Record recordObject)  {
        boolean modified = false;
        try {//Retrieve indicated Booking Object from the Database
            BookingRecord propertiesToUpdate = (BookingRecord) recordObject;
            Optional<Booking> bookingReturned = bookingRepository.findById(propertiesToUpdate.bookingNo());

            if (bookingReturned.isPresent()) {
                Booking booking = bookingReturned.get();
                BookingStatus status = booking.getStatus();

                if (propertiesToUpdate.status().equals(CANCELLED) && !propertiesToUpdate.status().equals(status)) {
                    booking.setStatus(CANCELLED);
                    modified = true;
                }
                else if (propertiesToUpdate.status().equals(USED) && !propertiesToUpdate.status().equals(status) && status.equals(VALID)) {
                    booking.setStatus(USED);
                    modified = true;
                    }

                if (modified) {
                    bookingRepository.save(booking);
                    return BookingRecord.copy(booking);
                } else {
                    return new BookingRecord(null, null, null, null, null,null,null,
                            "Reservation: " + booking.getBookingNo() + " could not be updated, please check your values" +
                                    " and dont update with the same information as before");
                }
            }
        } catch (Exception ex) {
           throw new RuntimeException("Booking Record could be updated, please try again later. Why? " + ex.getMessage());
        }
        return null;
    }

    //Returns true if the payment status was updated (to true or false)
    //Otherwise it returns false (most likely if the booking was not found)
    public Boolean updateIsPaid(UUID bookingNo, Boolean isPaid) {
        try {//Retrieve indicated Booking Object from the Database
            Optional<Booking> bookingReturned = bookingRepository.findById(bookingNo);
            if (bookingReturned.isPresent()) {
                Booking booking = bookingReturned.get();
                booking.setIsPaid(isPaid);
                bookingRepository.save(booking);
                return true;
            }
        }catch (Exception e){throw new RuntimeException(e.getMessage());}
        return false;
    }
}
