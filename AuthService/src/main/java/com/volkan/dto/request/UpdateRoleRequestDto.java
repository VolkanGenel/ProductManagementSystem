package com.volkan.dto.request;

import com.volkan.repository.enums.ERole;
import com.volkan.repository.enums.EStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleRequestDto {
    @NotBlank(message = "Token must be provided, please login first")
    String token;
    @Email(message = "Please provide a valid email")
    String email;
    @Enumerated(EnumType.STRING)
    ERole eRole;
    @Enumerated(EnumType.STRING)
    EStatus eStatus;
}
