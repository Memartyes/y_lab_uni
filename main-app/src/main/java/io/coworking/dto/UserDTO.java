package io.coworking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object для пользователей.
 */
@Data
public class UserDTO {

    private int id;

    @NotBlank(message = "Username cannot be blank")
    private String name;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 3, max = 30, message = "Password must be between 3 and 30 characters")
    private String password;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;
}
