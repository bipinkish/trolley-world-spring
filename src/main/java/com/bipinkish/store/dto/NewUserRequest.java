package com.bipinkish.store.dto;

import com.bipinkish.store.validations.Lowercase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewUserRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be at most 255 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Lowercase
    private String email;

    @Size(min = 6, max = 25, message = "Password must be between 6 and 25 characters")
    private String password;
}
