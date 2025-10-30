package pmf.bookingService.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingDTO {

    @NotNull(message = "Datum je obavezan")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "Vreme početka termina je obavezno")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull(message = "Vreme kraja termina je obavezno")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Size(max = 255, message = "Opis može imati najviše 255 karaktera")
    private String opisTermina;

}
