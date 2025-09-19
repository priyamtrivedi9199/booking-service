package com.otelier.backend.service;

import com.otelier.backend.exception.BookingConflictException;
import com.otelier.backend.model.Booking;
import com.otelier.backend.repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {
    
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
    
    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;
    
    public BookingService(BookingRepository bookingRepository, 
                         NotificationService notificationService) {
        this.bookingRepository = bookingRepository;
        this.notificationService = notificationService;
    }
    
    public List<Booking> getBookingsByHotel(String hotelId) {
        logger.info("Fetching bookings for hotel: {}", hotelId);
        return bookingRepository.findByHotelIdOrderByCheckInDateDesc(hotelId);
    }
    
    public List<Booking> getBookingsByHotelAndDateRange(String hotelId, 
                                                       LocalDate startDate, 
                                                       LocalDate endDate) {
        logger.info("Fetching bookings for hotel: {} from {} to {}", hotelId, startDate, endDate);
        return bookingRepository.findByHotelIdAndDateRange(hotelId, startDate, endDate);
    }
    
    @Transactional
    public Booking createBooking(Booking booking) {
        logger.info("Creating booking for hotel: {}, room: {}, dates: {} to {}", 
                   booking.getHotelId(), booking.getRoomNumber(), 
                   booking.getCheckInDate(), booking.getCheckOutDate());
        
        // Validate dates
        if (!booking.getCheckOutDate().isAfter(booking.getCheckInDate())) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
        
        // Check for conflicts
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
            booking.getHotelId(),
            booking.getRoomNumber(),
            booking.getCheckInDate(),
            booking.getCheckOutDate()
        );
        
        if (!conflicts.isEmpty()) {
            logger.warn("Booking conflict detected for hotel: {}, room: {}", 
                       booking.getHotelId(), booking.getRoomNumber());
            throw new BookingConflictException("Room is already booked for the selected dates");
        }
        
        Booking savedBooking = bookingRepository.save(booking);
        logger.info("Booking created successfully with ID: {}", savedBooking.getId());
        
        // Send notification
        try {
            notificationService.notifyBookingCreated(savedBooking);
        } catch (Exception e) {
            logger.error("Failed to send booking notification", e);
            // Don't fail the booking creation if notification fails
        }
        
        return savedBooking;
    }
}
