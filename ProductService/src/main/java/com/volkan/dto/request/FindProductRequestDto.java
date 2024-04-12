package com.volkan.dto.request;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindProductRequestDto {
    String id;
    String name;
    String description;

}
