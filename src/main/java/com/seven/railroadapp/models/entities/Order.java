package com.seven.railroadapp.models.entities;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {//TicketPayment
    private Double price;
    private String currency;
    private String method;
    private String intent;
    private String description;
    private Booking booking;
}
