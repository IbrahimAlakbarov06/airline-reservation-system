package org.airline.msflight.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "aircrafts")
public class Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String aircraftCode;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String manufacturer;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @Column(name = "economy_seats")
    private Integer economySeats;

    @Column(name = "business_seats")
    private Integer businessSeats;

    @Column(name = "first_class_seats")
    private Integer firstClassSeats;

    @Column(name = "max_range_km")
    private Integer maxRangeKm;

    @Column(name = "cruise_speed_kmh")
    private Integer cruiseSpeedKmh;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (aircraftCode != null) {
            aircraftCode = aircraftCode.toUpperCase();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();

        if (aircraftCode != null) {
            aircraftCode = aircraftCode.toUpperCase();
        }
    }

    public String getFullName() {
        return manufacturer + " " + model + " (" + aircraftCode + ")";
    }
}