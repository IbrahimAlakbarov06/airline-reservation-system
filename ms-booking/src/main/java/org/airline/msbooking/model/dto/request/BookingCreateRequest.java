package org.airline.msbooking.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingCreateRequest {

    @NotNull(message = "Flight ID is required")
    private Long flightId;

    @NotNull(message = "Flight class is required")
    private String flightClass;

    @NotNull(message = "Currency is required")
    private String currency = "USD";

    @NotEmpty(message = "At least one passenger is required")
    @Valid
    private List<PassengerDetailRequest> passengers;

    @NotNull(message = "Contact name is required")
    private String contactName;

    private String contactPhone;

    private String specialRequests;
}