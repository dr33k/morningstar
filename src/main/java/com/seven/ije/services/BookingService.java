package com.seven.ije.services;

import com.seven.ije.models.entities.*;
import com.seven.ije.models.enums.BookingStatus;
import com.seven.ije.models.enums.UserRole;
import com.seven.ije.models.enums.VoyageStatus;
import com.seven.ije.models.records.BookingRecord;
import com.seven.ije.models.records.VoyageRecord;
import com.seven.ije.models.requests.AppRequest;
import com.seven.ije.models.requests.BookingCreateRequest;
import com.seven.ije.models.requests.BookingUpdateRequest;
import com.seven.ije.repositories.BookingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.seven.ije.models.enums.BookingStatus.*;

@Service
@Transactional
@ApplicationScope
public class BookingService implements AppService <BookingRecord, AppRequest> {
    private BookingRepository bookingRepository;
    private VoyageService voyageService;
    private Authentication userAuthentication;

    public BookingService(BookingRepository bookingRepository ,
                          VoyageService voyageService ,
                          Authentication userAuthentication ) {
        this.bookingRepository = bookingRepository;
        this.voyageService = voyageService;
        this.userAuthentication = userAuthentication;
    }

    //Used by Admin
    @Override
    public Set <BookingRecord> getAll() {
        List <Booking> bookingList = bookingRepository.findAll();

        Set <BookingRecord> bookingRecords =
                bookingList.stream().map(BookingRecord::copy).collect(Collectors.toSet());

        return bookingRecords;
    }

    @Override
    public BookingRecord get(Object bookingNo) {
        User user = (User) userAuthentication.getPrincipal();

        Booking booking;
        if(user.getRole().equals(UserRole.ADMIN)) {
            booking = bookingRepository.findById((UUID) bookingNo)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,
                            "This reservation does not exist or has been deleted"));
        }
        else{
            booking = bookingRepository.findByPassengerIdAndBookingNo(user.getId() , (UUID) bookingNo)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,
                            "This reservation does not exist or has been deleted"));
        }
        return BookingRecord.copy(booking);
    }

    public Set <BookingRecord> getAllByPassenger(String email) {
        User user = (User) userAuthentication.getPrincipal();

        List <Booking> bookingList = (email == null)? //Indicates user accessing their account
                bookingRepository.findAllByPassengerId(user.getId()):
                bookingRepository.findAllByPassengerEmail(user.getEmail());

        Set <BookingRecord> bookingRecords = bookingList.stream().map(BookingRecord::copy).collect(Collectors.toSet());

        return bookingRecords;
    }

    @Override
    public BookingRecord create(AppRequest request) {
        try {
            BookingCreateRequest bookingRequest = (BookingCreateRequest) request;

            VoyageRecord voyageRecord = voyageService.get(bookingRequest.getVoyageNo());

            if (!voyageRecord.status().equals(VoyageStatus.PENDING))
                throw new ResponseStatusException(HttpStatus.CONFLICT ,
                        "This voyage has already begun or has been completed. It cannot be reserved.");

            Booking booking = new Booking();
            //Set Voyage
            booking.setVoyageNo(voyageRecord.voyageNo());
            //Set Passenger
            User user = (User) userAuthentication.getPrincipal();
            booking.setPassenger(user);
            //Set booking status
            booking.setStatus(BookingStatus.VALID);
            //Set payment status
            booking.setIsPaid(false);
            //Set booking date
            booking.setBookingDateTime(LocalDateTime.now());
            //Save
            bookingRepository.save(booking);

            return BookingRecord.copy(booking);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR ,
                    "Reservation could not be created,please contact System Administrator. Why? " + ex.getMessage());
        }
    }

    @Override
    public void delete(Object id) {
        UUID bookingNo = (UUID) id;
        User user = (User) userAuthentication.getPrincipal();

        if(user.getRole().equals(UserRole.ADMIN)) {
            if (bookingRepository.deleteByBookingNoAndStatusNot(bookingNo , VALID.name()) == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Delete could not be performed. Why?:" +
                        " \n 1) Please CANCEL first before deleting. " +
                        "\n2) This reservation has already been deleted");
            }
        } else {
            if (bookingRepository.deleteByPassengerIdAndBookingNoAndStatusNot(user.getId() , bookingNo , VALID.name()) == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Delete could not be performed. Why?:" +
                        " \n 1) Please CANCEL first before deleting. " +
                        "\n2) This reservation has already been deleted");
            }
        }
    }

    //For Admin
    @Override
    public BookingRecord update(AppRequest request) {
        try {
            BookingUpdateRequest newProperties = (BookingUpdateRequest) request;
            User user = (User) userAuthentication.getPrincipal();

            Booking booking = bookingRepository.findById(newProperties.getBookingNo())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,
                            "This reservation does not exist or has been deleted"));

            BookingStatus status = booking.getStatus();

            if (status.equals(EXPIRED) || status.equals(USED))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "This reservation has been used or is expired.");
            if (newProperties.getStatus().equals(status))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Please update with a different value");

            booking.setStatus(newProperties.getStatus());
            bookingRepository.save(booking);
            return BookingRecord.copy(booking);

        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR ,
                    "Reservation could not be updated, please contact System Administrator. Why? " + ex.getMessage());
        }
    }
    //For User
    public BookingRecord userUpdate(UUID bookingNo , Boolean cancel , Boolean isPaid) {
        User user = (User) userAuthentication.getPrincipal();
        //Retrieve indicated Booking Object from the Database
        Booking booking = bookingRepository.findByPassengerIdAndBookingNo(user.getId() , bookingNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,
                        "This reservation does not exist or has been deleted"));

        if (cancel) {
            booking.setStatus(CANCELLED);
            bookingRepository.save(booking);
        } else if (isPaid) {
            booking.setIsPaid(true);
            bookingRepository.save(booking);
        }
        return BookingRecord.copy(booking);
    }
}