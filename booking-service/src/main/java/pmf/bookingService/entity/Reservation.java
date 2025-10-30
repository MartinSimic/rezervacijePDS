package pmf.bookingService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name="reservation")
@Table(name="reservations",
        uniqueConstraints = {@UniqueConstraint(columnNames = "booking_id")}) // osigurava da termin može imati samo jednog korisnika
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timeReservation;

    @PrePersist
    void onCreate() {
        timeReservation = LocalDateTime.now();
    }
}
