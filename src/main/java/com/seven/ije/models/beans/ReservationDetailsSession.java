package com.seven.ije.models.beans;

import com.seven.ije.models.records.BookingRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.annotation.SessionScope;

public class ReservationDetailsSession {
    @Bean(name = "reservationDetails")
    @SessionScope
    public BookingRecord reservationDetails(){return new BookingRecord();}
}
