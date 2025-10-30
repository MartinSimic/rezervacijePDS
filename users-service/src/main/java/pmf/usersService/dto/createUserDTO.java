package pmf.usersService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class createUserDTO {
    @Size(max = 50)
    @NotBlank(message = "Ime je obavezno")
    private String name;

    @Size(max = 50)
    @NotBlank(message = "Email je obavezan")
    @Email(message="Polje mora biti tipa email")
    private String email;

    @Size(min=5)
    @NotBlank(message = "Sifra je obavezna")
    private String password;

    @Size(max = 50)
    @NotBlank(message = "Iznos je obavezan")
    private Double balance;
}
