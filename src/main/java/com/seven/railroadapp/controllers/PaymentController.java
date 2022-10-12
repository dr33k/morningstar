package com.seven.railroadapp.controllers;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.seven.railroadapp.models.entities.Booking;
import com.seven.railroadapp.models.entities.Order;
import com.seven.railroadapp.models.exceptions.PaymentException;
import com.seven.railroadapp.models.records.TicketRecord;
import com.seven.railroadapp.services.BookingService;
import com.seven.railroadapp.services.PaymentService;
import com.seven.railroadapp.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.ModelAndView;

import static com.seven.railroadapp.models.enums.PaymentMethod.*;
import static com.seven.railroadapp.models.enums.Currency.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private static final String DOMAIN_URL = "http://localhost:8080/payment";
    private static final String SUCCESS_URL = "/success"; // The success url will carry the bookingNo as a request parameter
    private static final String CANCEL_URL = "/cancel"; //Likewise the cancel url

    @Autowired
    PaymentService paymentService;
    @Autowired
    TicketService ticketService;
    @Autowired
    BookingService bookingService;

    @ModelAttribute("order")
    public Order orderObj(){return new Order();}

    @GetMapping
    public ModelAndView payment_landing(@ModelAttribute("bookingNo") UUID bookingNo, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) { throw new ResourceAccessException("Binding Result contained errors at the  /payment/ endpoint: "+bindingResult.getAllErrors().toString());}

            Booking booking = bookingService.getBookingEntity(bookingNo);
            Order order = Order.builder()
                    .price(paymentService.getPrice(Map.of(booking.getSeatType().toString(), 1)))
                    .currency(NGN.toString())
                    .method(PAYPAL.toString())
                    .intent("Ticket for Booking: " + booking.getBookingNo())
                    .description("Ticket for Voyage: " + booking.getVoyage().getVoyageNo())
                    .booking(booking).build();

            return new ModelAndView("payment_landing","orderFromController", order);
    }

    @PostMapping("/pay")
    public String payment(@ModelAttribute("order")Order order, BindingResult bindingResult)throws PaymentException{
        try{
            if(bindingResult.hasErrors())throw new ResourceAccessException("Binding Result contained errors at the  /tichetReservation/pay endpoint");
            Payment payment = paymentService.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(), order.getIntent(), order.getDescription(),
                    (DOMAIN_URL+CANCEL_URL+"?bookingNo="+order.getBooking().getBookingNo()),
                    (DOMAIN_URL+SUCCESS_URL+"?bookingNo="+order.getBooking().getBookingNo()));
            for(Links link:payment.getLinks()){
                if(link.getRel().equals("approval_url")){
                    return "redirect:"+link.getHref();
                }
            }
        }
        catch(PayPalRESTException e){throw new PaymentException(e);}
        return "redirect:/payment";
    }

    @GetMapping(SUCCESS_URL)
    public ModelAndView paymentSuccess(@RequestParam("paymentId")String paymentId, @RequestParam("payerId")String payerId, @RequestParam("bookingNo") UUID bookingNo)throws PaymentException{
        try {
            Payment payment = paymentService.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if(payment.getState().equals("approved")){
                //Create a Ticket Payment in the DB
                //Create a Ticket in the DB
                TicketRecord ticketRecord = ticketService.createByBookingNo(bookingNo);
                return new ModelAndView("payment_success","ticketRecord",ticketRecord);}
            else{
                ModelAndView mav = new ModelAndView("redirect:/payment");
                mav.addObject("message","Payment Not Approved");
                mav.addObject("bookingNo",bookingNo);
                return mav;
            }
        }
        catch (PayPalRESTException e){throw new PaymentException(e);}
    }

    @GetMapping(CANCEL_URL)
    public ModelAndView paymentCancel(@RequestParam("bookingNo") UUID bookingNo){
        ModelAndView mav = new ModelAndView("payment_cancel");
        mav.addObject("message","Payment Cancelled");
        mav.addObject("bookingNo",bookingNo);
        return mav;}
}
