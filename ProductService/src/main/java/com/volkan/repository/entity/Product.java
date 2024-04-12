package com.volkan.repository.entity;

import com.volkan.utility.StringPrefixedSequenceIdGenerator;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "tblproduct")
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @GenericGenerator(
            name = "product_seq",
            strategy = "com.volkan.utility.StringPrefixedSequenceIdGenerator",
            parameters = {
                    @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "P"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d")
            })
    String id;
    String name;
    String description;
    Double price;

}
