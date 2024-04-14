package com.volkan.dto.request;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveProductRequestDto {
    String name;
    String description;
    Double price;
}
