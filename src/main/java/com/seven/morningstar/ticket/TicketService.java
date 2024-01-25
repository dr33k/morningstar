package com.seven.morningstar.ticket;

import com.seven.morningstar.booking.Booking;
import com.seven.morningstar.booking.BookingService;
import com.seven.morningstar.AppService;
import com.seven.morningstar.enums.BookingUpdateType;
import com.seven.morningstar.user_management.User;
import com.seven.morningstar.enums.UserRole;
import com.seven.morningstar.voyage.VoyageRecord;
import com.seven.morningstar.AppRequest;
import com.seven.morningstar.voyage.VoyageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service("ticketService")
@Transactional
public class TicketService implements AppService <TicketRecord, AppRequest> {
    private TicketRepository ticketRepository;
    private BookingService bookingService;
    private VoyageService voyageService;
    private Authentication userAuthentication;
    private Booking reservationDetails;

    public TicketService(TicketRepository ticketRepository ,
                         BookingService bookingService,
                         Authentication userAuthentication,
                         VoyageService voyageService,
                         @Qualifier("reservationDetails") Booking reservationDetails) {
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
            ticket = ticketRepository.findByBookingPassengerIdAndBookingBookingNo(user.getId() , (UUID) bookingNo)
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
                ticket.setBooking(reservationDetails);
                //Set Creation Date
                ticket.setCreationDateTime(ZonedDateTime.now());
                //Set Expiry Date
                VoyageRecord voyageRecord = voyageService.get(this.reservationDetails.getVoyage());
                ticket.setExpiryDateTime(reservationDetails.getVoyage().getDepartureDateTime().plusDays(1L));
                //Save Booking and update reservationDetails
                this.reservationDetails = Booking.of(
                        bookingService.userUpdate(this.reservationDetails.getBookingNo(), BookingUpdateType.PAID));
                //Save Ticket
                ticketRepository.save(ticket);
                return TicketRecord.copy(ticket);

        }catch(Exception e){throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ticket could not be created, please contact system administrator. Why? "+e.getMessage());}
    }
}