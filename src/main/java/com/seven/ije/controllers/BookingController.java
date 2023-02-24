package com.seven.ije.controllers;

import com.seven.ije.models.entities.Booking;
import com.seven.ije.models.records.BookingRecord;
import com.seven.ije.models.requests.BookingCreateRequest;
import com.seven.ije.models.responses.Response;
import com.seven.ije.services.BookingService;
import com.seven.ije.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import static com.seven.ije.util.Responder.ok;

@RestController
@RequestMapping("/booking")
public class BookingController {
    BookingService bookingService;
    UserService userService;
    Booking reservationDetails;

    public BookingController(BookingService bookingService ,
                             UserService userService ,
                             @Qualifier("reservationDetails") Booking reservationDetails) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.reservationDetails = reservationDetails;
    }

    @GetMapping
    public ResponseEntity <Response> getAllResources() {
        Set <BookingRecord> bookingRecords = bookingService.getAllByPassenger(null);
        return ok(bookingRecords);
    }

    @GetMapping("/search")
    public ResponseEntity <Response> getResource(@Valid @RequestParam(name = "id") UUID id) {
        BookingRecord bookingRecord = bookingService.get(id);
        return ok(Set.of(bookingRecord));
    }

    @PostMapping("/create")
    public ModelAndView createResource(@Valid @RequestBody BookingCreateRequest request) throws Exception {
        ModelAndView mav = new ModelAndView("redirect:/payment");
        reservationDetails = Booking.of(request);
        return mav;
    }

    @PatchMapping("/cancel")
    public ResponseEntity <Response> cancel(@RequestParam(name = "id") UUID bookingNo) {
        BookingRecord bookingRecord = bookingService.userUpdate(bookingNo , true , false);
        return ok(Set.of(bookingRecord));
    }

    @DeleteMapping("/delete")
    public ResponseEntity <Response> deleteResource(@Valid @RequestParam(name = "id") UUID bookingNo) {
        bookingService.delete(bookingNo);
        return ok(Collections.emptySet());
    }
}