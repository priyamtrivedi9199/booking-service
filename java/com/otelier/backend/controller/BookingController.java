package com.otelier.backend.controller;

import com.otelier.backend.model.Booking;
import com.otelier.backend.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/hotels/{hotelId}/bookings")
@Tag(name = "Bookings", description = "Hotel booking management APIs")
@SecurityRequirement(name = "bearerAuth")
public class BookingController {
    
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
    
    private final BookingService bookingService;
    
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    
    @GetMapping
    @Operation(summary = "Get hotel bookings", 
               description = "Retrieve all bookings for a hotel, optionally filtered by date range")
    @PreAuthorize("hasAnyRole('STAFF', 'RECEPTION', 'ADMIN')")
    public ResponseEntity<List<Booking>> getBookings(
            @PathVariable String hotelId,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "Start date for filtering (YYYY-MM-DD)")
            LocalDate startDate,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "End date for filtering (YYYY-MM-DD)")
            LocalDate endDate,
            Authentication authentication) {
        
        logger.info("User {} requesting bookings for hotel: {}", 
                   authentication.getName(), hotelId);
        
        List<Booking> bookings;
        
        if (startDate != null && endDate != null) {
            if (endDate.isBefore(startDate)) {
                throw new IllegalArgumentException("End date must be after start date");
            }
            bookings = bookingService.getBookingsByHotelAndDateRange(hotelId, startDate, endDate);
        } else {
            bookings = bookingService.getBookingsByHotel(hotelId);
        }
        
        return ResponseEntity.ok(bookings);
    }
    
    @PostMapping
    @Operation(summary = "Create a new booking", 
               description = "Create a new booking with conflict detection")
    @PreAuthorize("hasAnyRole('STAFF', 'RECEPTION', 'ADMIN')")
    public ResponseEntity<Booking> createBooking(
            @PathVariable String hotelId,
            @Valid @RequestBody Booking booking,
            Authentication authentication) {
        
        logger.info("User {} creating booking for hotel: {}", 
                   authentication.getName(), hotelId);
        
        // Set hotel ID and user ID from path and authentication
        booking.setHotelId(hotelId);
        booking.setUserId(authentication.getName());
        
        Booking createdBooking = bookingService.createBooking(booking);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
    }
}
