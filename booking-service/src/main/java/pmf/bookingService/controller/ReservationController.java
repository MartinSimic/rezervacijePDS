package pmf.bookingService.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pmf.bookingService.dto.CompleteReservationDTO;
import pmf.bookingService.dto.ReservationDTO;
import pmf.bookingService.dto.UserDTO;
import pmf.bookingService.entity.Booking;
import pmf.bookingService.entity.Reservation;
import pmf.bookingService.repository.BookingRepository;
import pmf.bookingService.repository.ReservationRepository;
import pmf.bookingService.service.ReservationService;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService service;
    private final ReservationRepository reservationRepository;
    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;

    @PostMapping("/reserve")
    public ResponseEntity<?> makeReservation(@RequestBody ReservationDTO reservationDTO) {
        String message = "";
        Integer bookingId = reservationDTO.getBookingId();
        Integer userId    = reservationDTO.getUserId();

        if (!bookingRepository.existsById(bookingId)) {
            message += "Booking ne postoji.";

        }
        if (reservationRepository.existsByUserIdAndBookingId(userId, bookingId)) {
            message += "Duplikat rezervacije.";
        }

        if(service.fetchUser(userId) == null) {
            message += "User sa ID:" + userId + " ne postoji.\n";
        }
        else {

            Booking bookingRef = bookingRepository.getReferenceById(bookingId);

            Reservation r = new Reservation();
            r.setBooking(bookingRef);
            r.setUserId(userId);

            reservationRepository.save(r);
            message += "Uspešno dodata rezervacija.";
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(message);

    }


    @GetMapping
    public List<CompleteReservationDTO> getAllReseravations() {
        List<Reservation> reservations = reservationRepository.findAllFetchBooking();
        List<CompleteReservationDTO> result = new ArrayList<>(reservations.size());

        for (Reservation r : reservations) {
            CompleteReservationDTO dto = new CompleteReservationDTO();

            dto.setBookingId(r.getBooking().getId());
            dto.setUserId(r.getUserId());
            dto.setTimeReservation(r.getTimeReservation());

            Booking booking = r.getBooking();
            UserDTO user = service.fetchUser(r.getUserId());

            dto.setDate(booking.getDate());
            dto.setStartTime(booking.getStartTime());
            dto.setEndTime(booking.getEndTime());
            dto.setName(user.getName());

            result.add(dto);
        }
        return result;
    }


    @GetMapping("/{id}/user")
    public List<CompleteReservationDTO> getReservationByUserID(@PathVariable("id") Integer id) {
        UserDTO user = service.fetchUser(id);
        if (user == null) { return null; }
        List<Reservation> reservations = reservationRepository.findByUserIDFetchBooking(id);
        if( reservations.isEmpty() ) { return null; }

        List<CompleteReservationDTO> result = new ArrayList<>(reservations.size());
        for (Reservation r : reservations) {
            CompleteReservationDTO dto = new CompleteReservationDTO();

            dto.setBookingId(r.getBooking().getId());
            dto.setUserId(user.getId());
            dto.setTimeReservation(r.getTimeReservation());

            Booking booking = r.getBooking();
            dto.setDate(booking.getDate());
            dto.setStartTime(booking.getStartTime());
            dto.setEndTime(booking.getEndTime());
            dto.setName(user.getName());

            result.add(dto);
        }
        return result;
    }

}
