package com.celebrate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "tipping")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TippingEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36)
    private String id;

    @ElementCollection
    @CollectionTable(name = "tipping_variations", joinColumns = @JoinColumn(name = "tipping_id"))
    @Column(name = "tip_amount")
    private List<Double> tipVariations;

    @Column(nullable = false)
    private Boolean enabled;
}
