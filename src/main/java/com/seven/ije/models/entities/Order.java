package com.seven.ije.models.entities;

import com.seven.ije.models.records.BookingRecord;
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
    private BookingRecord bookingRecord;
}
