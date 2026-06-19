package com.celebrate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "restaurant_opening_times")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpeningTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    @Column(nullable = false, length = 20)
    private String day;

    @OneToMany(mappedBy = "openingTime", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TimingEntity> times;
}
