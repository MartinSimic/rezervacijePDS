package pmf.usersService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pmf.usersService.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u.name from user u")
    List<String> getAllUserNames();

    boolean existsByEmail(String email);
}
