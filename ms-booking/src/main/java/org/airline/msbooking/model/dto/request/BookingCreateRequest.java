package org.airline.msbooking.model.dto.request;

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
    private Long flightId;

    private String flightClass;

    private List<PassengerDetailRequest> passengers;

    private String contactName;

    private String contactPhone;
}
