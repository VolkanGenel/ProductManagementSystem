package com.volkan.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;



@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {
    @Email(message = "Please provide a valid email")
    String email;
    @NotBlank(message="Password requires to be not blank")
    @Size (min=8, max=64)
    @Pattern(
            message = "Password must be at least 8 characters, contain at least one uppercase, lowercase letter and a number.",
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")
    String password;
    String repassword;
}
