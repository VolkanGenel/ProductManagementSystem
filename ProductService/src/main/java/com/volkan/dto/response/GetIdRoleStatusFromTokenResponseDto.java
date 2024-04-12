package com.volkan.dto.response;

import com.volkan.repository.enums.ERole;
import com.volkan.repository.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetIdRoleStatusFromTokenResponseDto {
    Long id;
    ERole role;
    EStatus status;
}
