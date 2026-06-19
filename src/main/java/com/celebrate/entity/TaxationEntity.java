package com.celebrate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "taxation")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxationEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "taxation_charges")
    private Double taxationCharges;

    @Column(nullable = false)
    private Boolean enabled;
}
