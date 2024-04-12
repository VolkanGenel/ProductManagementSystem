package com.volkan.dto.request;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveProductRequestDto {
    String id;
    String name;
    String description;
    Double price;
}
