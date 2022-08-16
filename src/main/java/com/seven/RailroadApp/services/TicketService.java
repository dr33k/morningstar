package com.seven.RailroadApp.services;

import com.seven.RailroadApp.models.entities.Ticket;
import com.seven.RailroadApp.models.enums.BookingStatus;
import com.seven.RailroadApp.models.records.TicketRecord;
import com.seven.RailroadApp.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class TicketService implements com.seven.RailroadApp.services.Service {
    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public Set<TicketRecord> getAll() {
        Set<TicketRecord> ticketRecords = new HashSet<>(0);
        List<Ticket> ticketList = ticketRepository.findAll();
        for (Ticket ticket : ticketList) {
            TicketRecord ticketRecord = TicketRecord.copy(ticket);
            ticketRecords.add(ticketRecord);
        }
        return ticketRecords;
    }
    @Override
    public Record get(Object id) {
        try {
            Optional<Ticket> ticketReturned = ticketRepository.findById((Long) id);
            return ticketReturned.map(TicketRecord::copy).orElse(null);
        }catch (Exception ex){return null;}
    }
    public Boolean deleteByBookingNo(UUID voyageNo) {
        try {
            return ticketRepository.deleteByBooking(voyageNo);
        }catch(Exception ex){return false;}
    }
    @Override
    public Record update(Record recordObject) {
        try {
            TicketRecord propertiesToUpdate = (TicketRecord) recordObject;
            Optional<Ticket> ticketReturned = ticketRepository.findByBooking(propertiesToUpdate.bookingNo());

            if (ticketReturned.isPresent()) {
                Ticket ticket = ticketReturned.get();
                String status = propertiesToUpdate.status().name();

                switch(status){
                    case "CANCELLED": {ticketRepository.deleteById(ticket.getId());
                                        return TicketRecord.copy(ticket);}
                    case "USED": {ticket.setStatus(BookingStatus.USED);
                                    ticketRepository.save(ticket);
                                    return TicketRecord.copy(ticket);}
                    default:break;
                }
            }
        }catch (Exception ex){return null;}
        return null;
    }

    @Override
    public Record create(Record recordObject) {
        return null;
    }

    @Override
    public Boolean delete(Object id) {
        return null;
    }
}
