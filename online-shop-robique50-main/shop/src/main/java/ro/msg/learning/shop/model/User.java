package ro.msg.learning.shop.model;

import jakarta.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private String emailAddress;

    private String userRole;
}