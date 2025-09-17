package com.otelier.backend.service;

import com.otelier.backend.exception.BookingConflictException;
import com.otelier.backend.model.Booking;
import com.otelier.backend.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingServiceTest {
    
    @Mock
    private BookingRepository bookingRepository;
    
    @Mock
    private NotificationService notificationService;
    
    private BookingService bookingService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookingService = new BookingService(bookingRepository, notificationService);
    }
    
    @Test
    void createBooking_Success() {
        // Given
        Booking booking = new Booking("hotel1", "John Doe", "john@example.com", 
                                    "101", LocalDate.now().plusDays(1), 
                                    LocalDate.now().plusDays(3), new BigDecimal("200"), "user1");
        
        when(bookingRepository.findConflictingBookings(any(), any(), any(), any()))
            .thenReturn(Collections.emptyList());
        when(bookingRepository.save(any())).thenReturn(booking);
        
        // When
        Booking result = bookingService.createBooking(booking);
        
        // Then
        assertNotNull(result);
        verify(bookingRepository).save(booking);
        verify(notificationService).notifyBookingCreated(booking);
    }
    
    @Test
    void createBooking_ConflictDetected() {
        // Given
        Booking booking = new Booking("hotel1", "John Doe", "john@example.com", 
                                    "101", LocalDate.now().plusDays(1), 
                                    LocalDate.now().plusDays(3), new BigDecimal("200"), "user1");
        
        Booking existingBooking = new Booking();
        when(bookingRepository.findConflictingBookings(any(), any(), any(), any()))
            .thenReturn(Arrays.asList(existingBooking));
        
        // When & Then
        assertThrows(BookingConflictException.class, () -> bookingService.createBooking(booking));
        verify(bookingRepository, never()).save(any());
    }
}
