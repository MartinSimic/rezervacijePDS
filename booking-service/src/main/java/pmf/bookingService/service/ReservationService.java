package pmf.bookingService.service;

import pmf.bookingService.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationIntegrationService integration;

    public UserDTO fetchUser(Integer userID) {
        return integration.fetchUserOrThrow(userID);
    }

    public List<UserDTO> fetchAllUsers(){
        return integration.fetchAllUsersOrThrow();
    }

}
