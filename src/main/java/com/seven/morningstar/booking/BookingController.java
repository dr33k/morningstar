package com.seven.morningstar.booking;

import com.seven.morningstar.config.security.Authorize;
import com.seven.morningstar.responses.Response;
import com.seven.morningstar.user_management.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static com.seven.morningstar.util.AppConstants.VERSION;
import static com.seven.morningstar.util.Responder.ok;

@RestController
@RequestMapping(value = VERSION+"/booking", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name="jwtAuth")
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
    @Authorize(roles = "ADMIN")
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