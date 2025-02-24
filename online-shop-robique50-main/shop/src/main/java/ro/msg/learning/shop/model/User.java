package ro.msg.learning.shop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user")
public class User {
    @Id
    private UUID id;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private String emailAddress;

    private String userRole;
}