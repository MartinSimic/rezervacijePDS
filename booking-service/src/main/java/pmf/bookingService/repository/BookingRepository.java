package pmf.bookingService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pmf.bookingService.entity.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("SELECT b FROM booking b LEFT JOIN reservation r ON r.booking = b WHERE r.id IS NULL")
    List<Booking> findAvailableBookings();
}
