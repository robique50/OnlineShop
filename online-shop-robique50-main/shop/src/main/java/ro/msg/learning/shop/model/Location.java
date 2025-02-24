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
@Table(name = "location")
public class Location {
    @Id
    private UUID id;

    private String name;

    private String country;

    private String city;

    private String streetAddress;
}