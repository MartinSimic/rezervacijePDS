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
import java.util.Objects;

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

        Integer bookingId = reservationDTO.getBookingId();
        Integer userId    = reservationDTO.getUserId();

        if (!bookingRepository.existsById(bookingId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking ne postoji.");
        }
        if (reservationRepository.existsByUserIdAndBookingId(userId, bookingId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplikat rezervacije.");
        }
        service.fetchUser(userId);

        Booking bookingRef = bookingRepository.getReferenceById(bookingId);

        Reservation r = new Reservation();
        r.setBooking(bookingRef);
        r.setUserId(userId);

        reservationRepository.save(r);

        return ResponseEntity.status(HttpStatus.CREATED).body("Uspešno dodata rezervacija.");
    }


    @GetMapping
    public List<CompleteReservationDTO> getAllReseravations() {
        List<Reservation> reservations = reservationRepository.findAllFetchBooking();
        List<UserDTO> users = service.fetchAllUsers();
        List<CompleteReservationDTO> result = new ArrayList<>(reservations.size());

        for (Reservation r : reservations) {
            CompleteReservationDTO dto = new CompleteReservationDTO();

            dto.setBookingId(r.getBooking().getId());
            dto.setUserId(r.getUserId());
            dto.setTimeReservation(r.getTimeReservation());

            Booking booking = r.getBooking();
            modelMapper.map(booking, dto);
            for (UserDTO user : users) {
                if (Objects.equals(user.getId(), r.getUserId())) {
                    modelMapper.map(user, dto);; break;
                }
            }

            result.add(dto);
        }
        return result;
    }


    @GetMapping("/{id}/user")
    public List<CompleteReservationDTO> getReservationByUserID(@PathVariable("id") Integer id) {
        List<Reservation> reservations = reservationRepository.findByUserIDFetchBooking(id);

        UserDTO userDTO = service.fetchUser(id);

        List<CompleteReservationDTO> complete = new ArrayList<>(reservations.size());
        for (Reservation r : reservations) {
            CompleteReservationDTO dto = new CompleteReservationDTO();

            dto.setBookingId(r.getBooking().getId());
            dto.setUserId(r.getUserId());
            dto.setTimeReservation(r.getTimeReservation());

            Booking booking = r.getBooking();
            modelMapper.map(booking, dto);
            modelMapper.map(userDTO, dto);
            complete.add(dto);
        }
        return complete;
    }

}
