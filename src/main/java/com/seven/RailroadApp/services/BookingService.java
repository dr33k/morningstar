package com.seven.RailroadApp.services;

import com.seven.RailroadApp.models.entities.Booking;
import com.seven.RailroadApp.models.entities.User;
import com.seven.RailroadApp.models.enums.BookingStatus;
import com.seven.RailroadApp.models.enums.TicketStatus;
import com.seven.RailroadApp.models.records.BookingRecord;
import com.seven.RailroadApp.models.records.TicketRecord;
import com.seven.RailroadApp.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class BookingService extends com.seven.RailroadApp.services.Service {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private TicketService ticketService;

    @Override
    Set<BookingRecord> getAll() {
        Set<BookingRecord> bookingRecords = new HashSet<>(0);
        List<Booking> bookingList = bookingRepository.findAll();
        for (Booking booking : bookingList) {
            BookingRecord bookingRecord = BookingRecord.copy(booking);
            bookingRecords.add(bookingRecord);
        }
        return bookingRecords;
    }

    Set<BookingRecord> getAllByPassenger(User passenger){
        Set<BookingRecord> bookingRecords = new HashSet<>(0);
        Iterable<Booking> bookingList = bookingRepository.findAllByPassenger(passenger.getId());
        for (Booking booking : bookingList) {
            BookingRecord bookingRecord = BookingRecord.copy(booking);
            bookingRecords.add(bookingRecord);
        }
        return bookingRecords;
    }

    @Override
    Record get(Object id) {
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

    @Override
    Boolean delete(Object id) {
        try {
            Optional<Booking> bookingReturned = bookingRepository.findById((UUID) id);
            if (bookingReturned.isPresent()) {
                Boolean deletedTicket = ticketService.deleteByBookingNo(bookingReturned.get().getBookingNo());

                if (!deletedTicket) return false;

                bookingRepository.delete(bookingReturned.get());
                return true;
            }
        } catch (Exception ex) {return false;}
        return false;
    }

    @Override
    Record update(Record recordObject) {
        try {//Retrieve indicated Booking Object from the Database
            BookingRecord propertiesToUpdate = (BookingRecord) recordObject;
            Optional<Booking> bookingReturned = bookingRepository.findByBookingNo(propertiesToUpdate.bookingNo());

            if (bookingReturned.isPresent()) {
                Booking booking = bookingReturned.get();
                String status = booking.getStatus().name();

                if(status.equals("CANCELLED") && !propertiesToUpdate.status().name().equals(status))
                    return handleCancelled(booking);

            }
        } catch (Exception ex) {return null;}
        return null;
    }

    private Record handleCancelled(Booking booking){
        TicketRecord tr = new TicketRecord(booking.getBookingNo(),null, TicketStatus.CANCELLED);//Cancel/delete ticket
        Record r = ticketService.update(tr);
        if(r!=null) {
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);
            return BookingRecord.copy(booking);
        }
        else return null;
    }
}