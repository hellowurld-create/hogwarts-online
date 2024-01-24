package code.grind.giftedschoolonline.user.repository;

import code.grind.giftedschoolonline.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

}
