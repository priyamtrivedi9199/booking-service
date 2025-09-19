package com.otelier.backend.repository;

import com.otelier.backend.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
    
    List<Booking> findByHotelIdOrderByCheckInDateDesc(String hotelId);
    
    @Query("{'hotelId': ?0, 'checkInDate': {$gte: ?1}, 'checkOutDate': {$lte: ?2}}")
    List<Booking> findByHotelIdAndDateRange(String hotelId, LocalDate startDate, LocalDate endDate);
    
    @Query("{'hotelId': ?0, 'roomNumber': ?1, " +
           "$or': [" +
           "{'checkInDate': {$lt: ?3}, 'checkOutDate': {$gt: ?2}}, " +
           "{'checkInDate': {$gte: ?2, $lt: ?3}}, " +
           "{'checkOutDate': {$gt: ?2, $lte: ?3}}" +
           "], 'status': {$ne: 'CANCELLED'}}")
    List<Booking> findConflictingBookings(String hotelId, String roomNumber, 
                                        LocalDate checkInDate, LocalDate checkOutDate);
}
