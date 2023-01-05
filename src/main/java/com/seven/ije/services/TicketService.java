package com.seven.ije.services;

import com.seven.ije.models.entities.Booking;
import com.seven.ije.models.entities.Ticket;
import com.seven.ije.models.exceptions.ArbitraryException;
import com.seven.ije.models.records.BookingRecord;
import com.seven.ije.models.records.TicketRecord;
import com.seven.ije.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    BookingService bookingService;

    public Set<TicketRecord> getAll() {
        Set<TicketRecord> ticketRecords = new HashSet<>(0);
        List<Ticket> ticketList = ticketRepository.findAll();
        for (Ticket ticket : ticketList) {
            TicketRecord ticketRecord = TicketRecord.copy(ticket);
            ticketRecords.add(ticketRecord);
        }
        return ticketRecords;
    }
    public Record getByBookingNo(Object id) {
        try {
            Optional<Ticket> ticketReturned = ticketRepository.findByBookingBookingNo((UUID) id);
            return ticketReturned.map(TicketRecord::copy).orElse(null);
        }catch (Exception ex){throw new RuntimeException("Ticket not found. Why? "+ex.getMessage() );}
    }
    public Boolean deleteByBookingNo(UUID bookingNo) {
        try {
            Optional<Ticket> tOpt = ticketRepository.findByBookingBookingNo(bookingNo);
            if(tOpt.isPresent()){
                ticketRepository.deleteByBookingBookingNo(bookingNo);
                return true;
            }
        }catch(Exception ex){return false;}
        return false;
    }

    public TicketRecord createWith(BookingRecord reservationDetails){
        try {
            if(reservationDetails == null) throw new ArbitraryException("This should not be happening. ReservationDetails is null");

                //Create ticket
                Ticket ticket = new Ticket();
                //Set Booking
                ticket.setBookingNo(reservationDetails.bookingNo());
                //Set Expiry Date
                ticket.setExpiryDateTime((reservationDetails.voyage().travelDateTime().plusDays(1L)));
                //Save Booking and update reservationDetails
                reservationDetails = bookingService.userUpdate(reservationDetails.bookingNo(),false, true);
                //Save Ticket
                ticketRepository.save(ticket);
                return TicketRecord.copy(ticket);

        }catch(Exception e){throw new RuntimeException(e.getMessage());}
    }
    /*public Record update(Record recordObject) {
        try {
            TicketRecord propertiesToUpdate = (TicketRecord) recordObject;
            Optional<Ticket> ticketReturned = ticketRepository.findByBookingBookingNo(propertiesToUpdate.bookingNo());

            if (ticketReturned.isPresent()) {
                Ticket ticket = ticketReturned.get();
                BookingStatus status = propertiesToUpdate.status();

                switch (status) {
                    case CANCELLED -> {
                        ticketRepository.deleteById(ticket.getId());
                        return TicketRecord.copy(ticket);
                    }
                    case USED -> {
                        ticket.setStatus(BookingStatus.USED);
                        ticketRepository.save(ticket);
                        return TicketRecord.copy(ticket);
                    }
                    default -> {
                    }
                }
            }
        }catch (Exception ex){throw new RuntimeException("Ticket not found. Why? "+ex.getMessage()); }
        return null;
    }
    */

}