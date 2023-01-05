package com.seven.ije.services;

import com.seven.ije.models.entities.Booking;
import com.seven.ije.models.entities.Ticket;
import com.seven.ije.models.entities.User;
import com.seven.ije.models.enums.UserRole;
import com.seven.ije.models.records.BookingRecord;
import com.seven.ije.models.records.TicketRecord;
import com.seven.ije.models.records.VoyageRecord;
import com.seven.ije.models.requests.AppRequest;
import com.seven.ije.repositories.TicketRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.seven.ije.models.enums.BookingStatus.VALID;

@Service
@Transactional
public class TicketService implements AppService<TicketRecord, AppRequest> {
    private TicketRepository ticketRepository;
    private BookingService bookingService;
    private VoyageService voyageService;
    private Authentication userAuthentication;
    private BookingRecord reservationDetails;

    public TicketService(TicketRepository ticketRepository ,
                         BookingService bookingService,
                         Authentication userAuthentication,
                         VoyageService voyageService,
                         BookingRecord reservationDetails) {
        this.ticketRepository = ticketRepository;
        this.bookingService = bookingService;
        this.userAuthentication = userAuthentication;
        this.voyageService = voyageService;
        this.reservationDetails = reservationDetails;
    }

    //For Admin
    @Override
    public Set<TicketRecord> getAll() {
        List <Ticket> ticketList = ticketRepository.findAll();

        Set <TicketRecord> ticketRecords =
                ticketList.stream().map(TicketRecord::copy).collect(Collectors.toSet());

        return ticketRecords;
    }
    //For Both
    @Override
    public TicketRecord get(Object bookingNo) {
        User user = (User) userAuthentication.getPrincipal();

        Ticket ticket;
        if(user.getRole().equals(UserRole.ADMIN)) {
            ticket = ticketRepository.findByBookingBookingNo((UUID) bookingNo)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,
                            "This ticket does not exist or has been deleted"));
        }
        else{
            ticket = ticketRepository.findByBookingPassengerIdAndBookingNo(user.getId() , (UUID) bookingNo)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,
                            "This ticket does not exist or has been deleted"));
        }
        return TicketRecord.copy(ticket);
    }
    //For Both
    @Override
    public void delete(Object bookingNo) { return; } //A ticket is cascaded in the database when the associated booking is deleted
    @Override
    public TicketRecord create(AppRequest request){return null;}
    @Override
    public TicketRecord update(AppRequest request){return null;}
    public TicketRecord create(){
            if(this.reservationDetails == null) throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ticket could not be created. This should not be happening. reservationDetails is null");
            try{
                //Create ticket
                Ticket ticket = new Ticket();
                //Set Booking
                Booking booking = new Booking();
                BeanUtils.copyProperties(this.reservationDetails, booking);
                ticket.setBooking(booking);
                //Set Creation Date
                ticket.setCreationDateTime(LocalDateTime.now());
                //Set Expiry Date
                VoyageRecord voyageRecord = voyageService.get(this.reservationDetails.voyageNo());
                ticket.setExpiryDateTime((voyageRecord.travelDateTime().plusDays(1L)));
                //Save Booking and update reservationDetails
                this.reservationDetails = bookingService.userUpdate(this.reservationDetails.bookingNo(),false, true);
                //Save Ticket
                ticketRepository.save(ticket);
                return TicketRecord.copy(ticket);

        }catch(Exception e){throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ticket could not be created, please contact system administrator. Why? "+e.getMessage());}
    }
}