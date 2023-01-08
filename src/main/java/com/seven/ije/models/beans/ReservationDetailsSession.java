package com.seven.ije.models.beans;

import com.seven.ije.models.entities.Booking;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;
@Configuration
public class ReservationDetailsSession {
    @Bean(name = "reservationDetails")
    @SessionScope
    public Booking reservationDetails(){return new Booking();}
}
