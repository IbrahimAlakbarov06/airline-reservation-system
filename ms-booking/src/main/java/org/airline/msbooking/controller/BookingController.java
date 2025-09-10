package org.airline.msbooking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.airline.msbooking.model.dto.request.BookingCreateRequest;
import org.airline.msbooking.model.dto.response.BookingResponse;
import org.airline.msbooking.model.dto.response.PricingResponse;
import org.airline.msbooking.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @RequestBody @Valid BookingCreateRequest request,
            @RequestHeader("X-User-Id") String userIdHeader ) {
        BookingResponse booking = bookingService.createBooking(request, userIdHeader);

        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @PostMapping("/price-preview")
    public ResponseEntity<PricingResponse> getBookingPreview(
            @Valid @RequestBody BookingCreateRequest request) {
        PricingResponse response = bookingService.getBookingPricePreview(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingResponse>> getMyBookings(
            @RequestHeader("X-User-Id") String userIdHeader) {
        List<BookingResponse> bookingResponses = bookingService.getUserAllBookings(userIdHeader);
        return ResponseEntity.ok(bookingResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getMyBookingById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") String userIdHeader) {

        BookingResponse booking = bookingService.getUserBookingById(id, userIdHeader);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/cancel-booking/{id}")
    public ResponseEntity<String> cancelMyBooking(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") String userIdHeader) {

        bookingService.cancelBooking(id, userIdHeader);
        return ResponseEntity.ok("Booking successfully canceled");
    }
}
