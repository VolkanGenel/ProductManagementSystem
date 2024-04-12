package com.volkan.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseRequestDto {
    Integer pageSize;
    Integer currentPage;
    String sortParameter;
    String direction;

}
