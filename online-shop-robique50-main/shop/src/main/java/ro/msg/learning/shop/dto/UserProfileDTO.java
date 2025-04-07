package ro.msg.learning.shop.dto;

import java.util.UUID;

public record UserProfileDTO(UUID id, String username, String role) {
}