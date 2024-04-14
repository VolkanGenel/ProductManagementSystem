package com.volkan.repository.entity;

import com.volkan.repository.enums.ERole;
import com.volkan.repository.enums.EStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "tblauth")
public class Auth extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String email;
    String password;
    @Enumerated (EnumType.STRING)
    @Builder.Default
    private ERole role = ERole.USER_ROLE;
    @Builder.Default
    @Enumerated (EnumType.STRING)
    private EStatus status=EStatus.ACTIVE;
}
