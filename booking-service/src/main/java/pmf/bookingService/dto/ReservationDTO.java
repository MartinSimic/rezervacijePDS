package pmf.bookingService.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservationDTO {

    @NotBlank(message="Booking id mora biti unet")
    private Integer bookingId;
    @NotBlank(message="User id mora biti unet")
    private Integer userId;


}
