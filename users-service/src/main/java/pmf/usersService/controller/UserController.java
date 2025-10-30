package pmf.usersService.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pmf.usersService.dto.UserDTO;
import pmf.usersService.dto.createUserDTO;
import pmf.usersService.dto.updateUserDTO;
import pmf.usersService.entity.User;
import pmf.usersService.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<UserDTO> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> usersDTO = users.stream().map(user -> modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
        return usersDTO;
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Integer id) {
        User user = userRepository.findById(id).orElse(null);
        if(user == null){
            return null;
        }
        UserDTO userDTO = modelMapper.map(user,UserDTO.class);
        return  userDTO;
    }

    @GetMapping("/names")
    public List<String> getUserNames() {
        return userRepository.getAllUserNames();
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody createUserDTO user, BindingResult result) {

        if(result.hasErrors()){
            List<String> errors = result.getFieldErrors().stream().map(error->error.getDefaultMessage()).toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        if(userRepository.existsByEmail(user.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email vec postoji");
        }
        User userNew = modelMapper.map(user,User.class);
        userRepository.save(userNew);
        return ResponseEntity.status(HttpStatus.CREATED).body("Korisnik je uspesno kreiran");

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id){
        if(userRepository.existsById(id))
        {
            userRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Korisnik sa ID "+id+" je obrisan");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Korisnik sa ID "+id+" nije pronadjen");

    }

    @PatchMapping("/balance")
    public ResponseEntity<?> updateBalanceById(@Valid @RequestBody updateUserDTO newUserBalance, BindingResult result){
        Integer id = newUserBalance.getId();
        if(result.hasErrors()){
            List<String> errors = result.getFieldErrors().stream().map(error->error.getDefaultMessage()).toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        User user = userRepository.findById(id).orElse(null);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Korisnik sa ID "+id+" nije pronadjen.");
        }
        Double newBalance = newUserBalance.getBalance();
        user.setBalance(newBalance);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("Iznos je uspesno promenjen na: "+newUserBalance.getBalance());
    }
}
