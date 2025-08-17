package org.airline.msflight.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.airline.msflight.model.enums.FlightStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "flights")
@Builder
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aircraft_id", nullable = false)
    private Aircraft aircraft;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_airport_id", nullable = false)
    private Airport originAirport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_airport_id", nullable = false)
    private Airport destinationAirport;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FlightStatus status;

    @Column(name = "base_price", precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "economy_price", precision = 10, scale = 2)
    private BigDecimal economyPrice;

    @Column(name = "business_price", precision = 10, scale = 2)
    private BigDecimal businessPrice;

    @Column(name = "first_class_price", precision = 10, scale = 2)
    private BigDecimal firstClassPrice;

    @Column(name = "available_economy_seats")
    private Integer availableEconomySeats;

    @Column(name = "available_business_seats")
    private Integer availableBusinessSeats;

    @Column(name = "available_first_class_seats")
    private Integer availableFirstClassSeats;

    @Column(name = "gate_number")
    private String gateNumber;

    @Column(name = "terminal")
    private String terminal;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (aircraft != null) {
            if (availableEconomySeats == null) {
                availableEconomySeats = aircraft.getEconomySeats();
            }
            if (availableBusinessSeats == null) {
                availableBusinessSeats = aircraft.getBusinessSeats();
            }
            if (availableFirstClassSeats == null) {
                availableFirstClassSeats = aircraft.getFirstClassSeats();
            }
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Integer getTotalAvailableSeats() {
        return availableEconomySeats + availableBusinessSeats + availableFirstClassSeats;
    }

    public boolean hasAvailableSeats(String flightClass, Integer requestedSeats) {
        return switch (flightClass.toUpperCase()) {
            case "ECONOMY" -> availableEconomySeats >= requestedSeats;
            case "BUSINESS" -> availableBusinessSeats >= requestedSeats;
            case "FIRST_CLASS" -> availableFirstClassSeats >= requestedSeats;
            default -> false;
        };
    }
}
