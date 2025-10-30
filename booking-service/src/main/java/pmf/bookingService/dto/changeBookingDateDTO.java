package pmf.bookingService.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class changeBookingDateDTO {

    @NotNull(message="Datum je obavezan")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
