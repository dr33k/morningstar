package com.seven.morningstar.backend.config;

import com.seven.morningstar.backend.booking.Booking;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;
@Configuration
public class ReservationDetailsSession {
    @Bean(name = "reservationDetails")
    @SessionScope
    public Booking reservationDetails(){return new Booking();}
}
