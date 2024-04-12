package com.volkan.repository.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity {
      Long createat;
      Long updateat;

}
