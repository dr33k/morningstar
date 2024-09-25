package com.seven.morningstar.backend.booking;

import com.seven.morningstar.backend.location.enums.BookingUpdateType;
import com.seven.morningstar.backend.responses.Response;
import com.seven.morningstar.backend.user_management.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

import static com.seven.morningstar.backend.util.AppConstants.VERSION;
import static com.seven.morningstar.backend.util.Responder.*;

@RestController
@RequestMapping(value = VERSION+"/booking", produces = "application/json")
@OpenAPIDefinition(tags = @Tag(name="booking-controller"))
@SecurityRequirement(name="jwtAuth")
public class BookingController {
    BookingService bookingService;
    UserService userService;
    Booking reservationDetails;

    public BookingController(BookingService bookingService ,
                             UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('booking:r')")
    public ResponseEntity <Response> getAllResources() {
        Set <BookingRecord> bookingRecords = bookingService.getAllByPassenger(null);
        return ok(bookingRecords);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('booking:r')")
    public ResponseEntity <Response> getResource(@Valid @RequestParam(name = "id") UUID id) {
        return ok(bookingService.get(id));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('booking:c')")
    public ResponseEntity <Response> createResource(@Valid @RequestBody BookingCreateRequest request) throws Exception {
//        ModelAndView mav = new ModelAndView("redirect:/payment");
//        reservationDetails = Booking.of(request);
//        return mav;

        return created(bookingService.create(request), VERSION+"/booking");
    }

    @PatchMapping("/cancel")
    @PreAuthorize("hasAuthority('booking:u')")
    public ResponseEntity <Response> cancel(@RequestParam(name = "id") UUID bookingNo) {
        return ok(bookingService.userUpdate(bookingNo , BookingUpdateType.CANCEL));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('booking:d')")
    public ResponseEntity <Response> deleteResource(@Valid @RequestParam(name = "id") UUID bookingNo) {
        bookingService.delete(bookingNo);
        return noContent();
    }
}