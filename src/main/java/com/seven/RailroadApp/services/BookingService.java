package com.seven.RailroadApp.services;

import com.seven.RailroadApp.config.security.UserAuthentication;
import com.seven.RailroadApp.models.entities.*;
import com.seven.RailroadApp.models.enums.BookingStatus;
import com.seven.RailroadApp.models.enums.SeatType;
import com.seven.RailroadApp.models.records.*;
import com.seven.RailroadApp.models.records.BookingRecord;
import com.seven.RailroadApp.repositories.BookingRepository;
import com.seven.RailroadApp.repositories.UserRepository;
import com.seven.RailroadApp.repositories.VoyageRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import static com.seven.RailroadApp.models.enums.BookingStatus.*;
@Service
@Transactional
public class BookingService implements com.seven.RailroadApp.services.Service {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private VoyageRepository voyageRepository;
    @Autowired
    private UserAuthentication userAuthentication;
    @Autowired
    private TicketService ticketService;

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
            return null;
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

            Optional<Voyage> vOpt=  voyageRepository.findById(bookingRecord.voyageNo());

            if (vOpt.isPresent()) {
                //Set Voyage
                booking.setVoyage(vOpt.get());
                //Set Passenger
                User user = (User) userAuthentication.getInstance().getPrincipal();
                booking.setPassenger(user);
                //Set booking status
                booking.setStatus(BookingStatus.VALID);
                //Set booking no
                booking.setBookingNo(UUID.randomUUID());
                //Set booking date
                booking.setBookingDate(LocalDateTime.now());
                //Set expiry date
                booking.setExpiryDate(vOpt.get().getTravelDate().plusDays(1L));
                //Save
                bookingRepository.save(booking);

                return BookingRecord.copy(booking);
            }
        } catch (Exception ex) {
            return new BookingRecord(null, null, null, null, null, null, null,
                    "Reservation could be created, please try again later. Why? " + ex.getMessage());
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

                if (propertiesToUpdate.status().equals(CANCELLED) && !propertiesToUpdate.status().equals(status)) {
                    //Delete ticket
                    Boolean deleted = ticketService.deleteByBookingNo(booking.getBookingNo());
                    if (deleted) {
                        booking.setStatus(CANCELLED);
                        modified = true;
                    }
                } else if (propertiesToUpdate.status().equals(USED) && !propertiesToUpdate.status().equals(status)) {
                    //Update ticket to used also
                    TicketRecord tr = new TicketRecord(booking.getBookingNo(), null, BookingStatus.USED);
                    tr = (TicketRecord) ticketService.update(tr);
                    if (tr != null) {
                        booking.setStatus(BookingStatus.USED);
                        modified = true;
                    }
                }

                if (modified) {
                    bookingRepository.save(booking);
                    return BookingRecord.copy(booking);
                } else {
                    return new BookingRecord(null, null, null, null, null, null,null,
                            "Reservation: " + booking.getBookingNo() + " could be updated, please check your values" +
                                    " and dont update with the same information as before");
                }
            }
        } catch (Exception ex) {
            return new BookingRecord(null, null, null, null, null, null,null,
                    "Booking Record could be updated, please try again later. Why? " + ex.getMessage());
        }
        return null;
    }
}
