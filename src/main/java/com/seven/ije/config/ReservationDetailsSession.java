package com.seven.ije.config;

import com.seven.ije.booking.Booking;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;
@Configuration
public class ReservationDetailsSession {
    @Bean(name = "reservationDetails")
    @SessionScope
    public Booking reservationDetails(){return new Booking();}
}
