package pmf.usersService.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class updateUserDTO {

    @Id
    private Integer id;
    @NotNull(message = "Iznos je obavezan")
    private Double balance;

}
