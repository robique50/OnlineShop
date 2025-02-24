package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.msg.learning.shop.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository  extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);
}