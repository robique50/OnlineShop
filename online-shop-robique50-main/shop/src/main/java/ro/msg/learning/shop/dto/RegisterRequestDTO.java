package ro.msg.learning.shop.dto;

public record RegisterRequestDTO(
    String username,
    String password,
    String firstName,
    String lastName,
    String emailAddress,
    String role
) {}
