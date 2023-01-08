package com.seven.ije.controllers;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.seven.ije.models.entities.Booking;
import com.seven.ije.models.entities.Order;
import com.seven.ije.models.records.TicketRecord;
import com.seven.ije.services.BookingService;
import com.seven.ije.services.PaymentService;
import com.seven.ije.services.TicketService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import static com.seven.ije.models.enums.PaymentMethod.*;
import static com.seven.ije.models.enums.Currency.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/payment")
@ApplicationScope
public class PaymentController {
    private static final String DOMAIN_URL = "http://127.0.0.1:8080/payment";
    private static final String SUCCESS_URL = "/success"; // The success url will carry the bookingNo as a request parameter
    private static final String CANCEL_URL = "/cancel"; //Likewise the cancel url

    PaymentService paymentService;
    TicketService ticketService;
    BookingService bookingService;
    Booking reservationDetails;

    public PaymentController(PaymentService paymentService , TicketService ticketService , BookingService bookingService , @Qualifier("reservationDetails") Booking reservationDetails) {
        this.paymentService = paymentService;
        this.ticketService = ticketService;
        this.bookingService = bookingService;
        this.reservationDetails = reservationDetails;
    }

    private Order order;

//    @ModelAttribute("order")
//    public Order orderObj(){return new Order();}

    @GetMapping
    public ModelAndView payment_landing(@ModelAttribute("displayWarning") String displayWarning, BindingResult bindingResult, ModelMap modelMap) {
        //displayWarnings are just in case there is no approval_url after creating payment. It redirects here
        if(bindingResult.hasErrors()){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "[GET: /payment] BindingResult has errors" + bindingResult.getAllErrors().stream().findFirst().get());}

        this.order = Order.builder()
                    .price(paymentService.getPrice(Map.of(reservationDetails.getSeatType().toString(), 1)))
                    .currency(NGN.toString())
                    .method(PAYPAL.toString())
                    .intent("Ticket for Booking: " + reservationDetails.getBookingNo())
                    .description("Ticket for Voyage: " + reservationDetails.getVoyage().getVoyageNo())
                    .booking(reservationDetails).build();

            modelMap.put("orderFromController", this.order);
            modelMap.put("displayWarning", displayWarning);

            return new ModelAndView("payment_landing","model", modelMap);
    }

    @PostMapping("/pay")
    public String payment(ModelMap modelMap){
        try{
            Payment payment = paymentService.createPayment(this.order.getPrice(), this.order.getCurrency(), this.order.getMethod(), this.order.getIntent(), this.order.getDescription(),
                    (DOMAIN_URL+CANCEL_URL+"?bookingNo="+order.getBooking().getBookingNo()),
                    (DOMAIN_URL+SUCCESS_URL+"?bookingNo="+order.getBooking().getBookingNo()));
            for(Links link:payment.getLinks()){
                if(link.getRel().equals("approval_url")){
                    return "redirect:"+link.getHref();
                }
            }
            modelMap.put("displayWarning","Apparently there were  no approval urls from your payments. You should look into that");
            return "redirect:/payment";
        }
        catch(PayPalRESTException e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "[POST: /payment/pay ] "+e.getMessage(),e);}
    }

    @GetMapping(SUCCESS_URL)
    public ModelAndView paymentSuccess(@RequestParam("paymentId")String paymentId, @RequestParam("payerId")String payerId){
        try {
            Payment payment = paymentService.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if(payment.getState().equals("approved")){
                //Create a Ticket Payment in the DB

                //Create a Ticket in the DB
                TicketRecord ticketRecord = ticketService.create();
                return new ModelAndView("payment_success","ticketRecord",ticketRecord);
            }

            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED,
                    String.format("[GET: /payment%s] Payment Not Approved",SUCCESS_URL));
        }
        catch (PayPalRESTException e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format("[GET: /payment%s] Payment Not Approved. Why? %s",SUCCESS_URL,e.getMessage()), e);}
    }

    @GetMapping(CANCEL_URL)
    public ModelAndView paymentCancel(@RequestParam("bookingNo") UUID bookingNo){
        ModelAndView mav = new ModelAndView("payment_cancel");
        mav.addObject("message","Payment Cancelled");
        mav.addObject("bookingNo",bookingNo);
        return mav;}
}
