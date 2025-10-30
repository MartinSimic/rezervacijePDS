package pmf.bookingService.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pmf.bookingService.dto.BookingDTO;
import pmf.bookingService.dto.changeBookingDateDTO;
import pmf.bookingService.entity.Booking;
import pmf.bookingService.repository.BookingRepository;
import pmf.bookingService.repository.ReservationRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingRepository bookingRepository;
    private final ReservationRepository reservationRepository;

    private final ModelMapper modelMapper;

    @GetMapping
    public List<BookingDTO> getBookings() {
        List<Booking> avalibeBooking = bookingRepository.findAvailableBookings();
        return avalibeBooking.stream().map(booking -> modelMapper.map(booking,BookingDTO.class)).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public boolean isAvailable(@PathVariable Integer id) {
        List<Booking> availableBookings = bookingRepository.findAvailableBookings();
        return availableBookings.stream().anyMatch(booking -> booking.getId().equals(id));
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingDTO booking, BindingResult result) {
        if(result.hasErrors()){
            List<String> errors = result.getFieldErrors().stream().map(error->error.getDefaultMessage()).toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        Booking bookingNew = modelMapper.map(booking,Booking.class);
        bookingRepository.save(bookingNew);
        return ResponseEntity.status(HttpStatus.CREATED).body("Korisnik je uspesno kreiran");
    }

    @PatchMapping("/changeDate/{id}")
    public ResponseEntity<?> changeBooking(@Valid @RequestBody changeBookingDateDTO dto, @PathVariable Integer id, BindingResult result){
        if(result.hasErrors()){
            List<String> errors = result.getFieldErrors().stream().map(error->error.getDefaultMessage()).toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        Booking booking = bookingRepository.findById(id).orElse(null);
        if(booking==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ne postoji booking za id:"+id);
        }
        booking.setDate(dto.getDate());
        bookingRepository.save(booking);
        return ResponseEntity.status(HttpStatus.OK).body("Uspesno azuriran booking");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookingByID(@PathVariable Integer id){
        if(reservationRepository.existsById(id))
            reservationRepository.deleteById(id);

        if(bookingRepository.existsById(id)){
            bookingRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Uspesno obrisan termin sa id-jem: "+id);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ne postoji termin sa id-jem: "+id);

    }
}
