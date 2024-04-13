package com.volkan.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequestDto {
    private String email;
    private String password;
    @Pattern( message = "Password requires to be with at least eight characters." +
            " It must includes at least one lower, one upper character and a number",
            regexp = "^(?=.*[0-12])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")
    private String newPassword;
    private String reNewPassword;
}
