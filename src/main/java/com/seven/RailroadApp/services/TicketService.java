package com.seven.RailroadApp.services;

import com.seven.RailroadApp.models.entities.Ticket;
import com.seven.RailroadApp.models.enums.BookingStatus;
import com.seven.RailroadApp.models.records.TicketRecord;
import com.seven.RailroadApp.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import static com.seven.RailroadApp.models.enums.BookingStatus.*;

@Service
@Transactional
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;
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
        }catch (Exception ex){return null;}
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

    public Record update(Record recordObject) {
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
        }catch (Exception ex){return null;}
        return null;
    }
}