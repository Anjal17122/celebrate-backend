package com.celebrate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "food_variations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariationEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Double price;

    private Double discounted;

    @Column(name = "is_out_of_stock")
    private Boolean isOutOfStock;

    @Column(name = "prep_time")
    private Integer prepTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private FoodEntity food;

    // Addon references stored as IDs (many-to-many via string)
    @ElementCollection
    @CollectionTable(name = "variation_addons", joinColumns = @JoinColumn(name = "variation_id"))
    @Column(name = "addon_id", length = 36)
    private List<String> addonIds;
}
