package pmf.bookingService.repository;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pmf.bookingService.entity.Reservation;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query("select r from reservation r join fetch r.booking where r.userId = :userID")
    List<Reservation> findByUserIDFetchBooking(@Param("userID") Integer userId);

    @Query("select r from reservation r join fetch r.booking")
    List<Reservation> findAllFetchBooking();

    boolean existsByUserIdAndBookingId(Integer userId, Integer bookingId);
}
