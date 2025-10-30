package pmf.bookingService.service;

import pmf.bookingService.dto.UserDTO;
import pmf.bookingService.feign.UserClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationIntegrationService {

    private final UserClient usersClient;

    @Retry(name = "externalRetry")
    @CircuitBreaker(name = "externalCb", fallbackMethod = "getUserFallback")
    public UserDTO fetchUserOrThrow(Integer userId) {
        log.info("Poziv users-service-a (Feign) userId="+ userId);
        return usersClient.getUserById(userId).getBody(); // Feign poziv
    }

    public UserDTO getUserFallback(Integer userId, Throwable t) {
        log.warn("CB fallback pri trazenju korisnika sa id-jem = "+ userId+" cause="+t.toString());
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Users service trenutno nije dostupan (CB fallback)", t);
    }

    @Retry(name="externalRetry")
    @CircuitBreaker(name="externalCb",fallbackMethod = "getReservationsFallback")
    public List<UserDTO> fetchAllUsersOrThrow(){
        log.info("Poziv users-service-a (Feign)");
        return usersClient.getAllUsers();
    }

    public List<UserDTO> getReservationsFallback(Throwable t) {
        log.warn("CB fallback pri trazenju svih korisnika cause="+t.toString());
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Users service nedostupan (CB fallback)", t);
    }
}

