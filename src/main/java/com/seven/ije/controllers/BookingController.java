package com.seven.ije.controllers;

import com.seven.ije.config.security.UserAuthentication;
import com.seven.ije.models.entities.User;
import com.seven.ije.models.enums.BookingStatus;
import com.seven.ije.models.records.BookingRecord;
import com.seven.ije.models.requests.BookingCreateRequest;
import com.seven.ije.models.requests.BookingUpdateRequest;
import com.seven.ije.models.responses.Response;
import com.seven.ije.services.BookingService;
import com.seven.ije.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/booking")
public class BookingController {
    BookingService bookingService;
    UserService userService;
    BookingRecord reservationDetails;

    public BookingController(BookingService bookingService ,
                             UserService userService ,
                             @Qualifier("reservationDetails") BookingRecord reservationDetails) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.reservationDetails = reservationDetails;
    }

    @GetMapping
    public ResponseEntity <Response> getAllResources() {
        Set <BookingRecord> bookingRecords = bookingService.getAllByPassenger(null);
        return ResponseEntity.ok(Response.builder()
                .data(bookingRecords)
                .isError(false)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/search")
    public ResponseEntity <Response> getResource(@Valid @RequestParam(name = "id") UUID id) {
        BookingRecord bookingRecord = (BookingRecord) bookingService.get(id);

        return ResponseEntity.ok(Response.builder()
                .data(Set.of(bookingRecord))
                .isError(false)
                .status(HttpStatus.FOUND)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/create")
    public ModelAndView createResource(@Valid @RequestBody BookingCreateRequest request) throws Exception {
        BookingRecord bookingRecord = (BookingRecord) bookingService.create(request);

        ModelAndView mav = new ModelAndView("redirect:/payment");
        reservationDetails = bookingRecord;
        return mav;
    }

    @PatchMapping("/cancel")
    public ResponseEntity <Response> cancel(@RequestParam(name = "id") UUID bookingNo) {
        BookingRecord bookingRecord = bookingService.userUpdate(bookingNo , true , false);
        return ResponseEntity.ok(Response.builder()
                .data(Set.of(bookingRecord))
                .isError(false)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity <Response> deleteResource(@Valid @RequestParam(name = "id") UUID bookingNo) {
        bookingService.delete(bookingNo);

        return ResponseEntity.ok(Response.builder()
                .message("Booking: " + bookingNo + " deleted successfully")
                .isError(false)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build());
    }
}