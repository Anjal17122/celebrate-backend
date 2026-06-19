package com.celebrate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "addons")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddonEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "quantity_minimum", nullable = false)
    private Integer quantityMinimum;

    @Column(name = "quantity_maximum", nullable = false)
    private Integer quantityMaximum;

    @Column(name = "is_out_of_stock")
    private Boolean isOutOfStock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    // Option IDs referenced
    @ElementCollection
    @CollectionTable(name = "addon_options", joinColumns = @JoinColumn(name = "addon_id"))
    @Column(name = "option_id", length = 36)
    private List<String> optionIds;
}
