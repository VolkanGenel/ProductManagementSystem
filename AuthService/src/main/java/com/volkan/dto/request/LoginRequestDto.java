package com.volkan.dto.request;

import lombok.*;



@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
    String email;
    String password;
}
