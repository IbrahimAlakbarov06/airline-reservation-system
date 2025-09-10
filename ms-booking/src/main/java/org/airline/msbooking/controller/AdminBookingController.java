package org.airline.msbooking.controller;

import lombok.RequiredArgsConstructor;
import org.airline.msbooking.model.dto.response.BookingResponse;
import org.airline.msbooking.model.dto.response.PassengerDetailResponse;
import org.airline.msbooking.model.enums.BookingStatus;
import org.airline.msbooking.service.AdminBookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/bookings")
@RequiredArgsConstructor
public class AdminBookingController {

    private final AdminBookingService adminBookingService;

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookingResponse> bookings = adminBookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
        BookingResponse booking = adminBookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/code/{bookingCode}")
    public ResponseEntity<BookingResponse> getBookingByCode(@PathVariable String bookingCode) {
        BookingResponse booking = adminBookingService.getByBookingCode(bookingCode);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BookingResponse>> getBookingsByStatus(@PathVariable BookingStatus status) {
        List<BookingResponse> bookings = adminBookingService.getBookingByStatus(status);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/flight/{flightId}")
    public ResponseEntity<List<BookingResponse>> getBookingsByFlight(@PathVariable Long flightId) {
        List<BookingResponse> bookings = adminBookingService.getBookingByFlightId(flightId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponse>> getBookingsByUser(@PathVariable Long userId) {
        List<BookingResponse> bookings = adminBookingService.getBookingsByUser(userId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/passenger/{bookingId}")
    public ResponseEntity<List<PassengerDetailResponse>> getPassengersByBookingId(@PathVariable Long bookingId) {
        List<PassengerDetailResponse> passengers = adminBookingService.findPassengersByBookingId(bookingId);
        return ResponseEntity.ok(passengers);
    }

    @GetMapping("/passenger/{userProfileId}")
    public ResponseEntity<List<PassengerDetailResponse>> getPassengersByUserProfileId(@PathVariable Long userProfileId) {
        List<PassengerDetailResponse> passengers = adminBookingService.findByUserProfileId(userProfileId);
        return ResponseEntity.ok(passengers);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<BookingResponse>> getRecentBookings(
            @RequestParam(defaultValue = "24") Integer hours) {
        List<BookingResponse> bookings = adminBookingService.getRecentBookings(hours);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<BookingResponse> confirmBooking(@PathVariable Long id) {
        BookingResponse booking = adminBookingService.confirmBooking(id);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        adminBookingService.cancelBooking(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<BookingResponse> completeBooking(@PathVariable Long id) {
        BookingResponse booking = adminBookingService.completeBooking(id);
        return ResponseEntity.ok(booking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        adminBookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}