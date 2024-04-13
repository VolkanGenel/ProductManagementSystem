package com.volkan.dto.response;

import com.volkan.repository.enums.ERole;
import com.volkan.repository.enums.EStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindAllResponseDto {
    private Long id;
    private String email;
    private ERole role;
    private EStatus status;
}
