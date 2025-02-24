package ro.msg.learning.shop.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@Setter
@Table(name = "placed_order")
@AllArgsConstructor
@NoArgsConstructor
public class PlacedOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private User customer;

    private LocalDateTime createdAt;

    private String country;

    private String city;

    private String streetAddress;
}