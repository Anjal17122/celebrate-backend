package com.celebrate.entity;

import com.celebrate.utils.PolygonConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "zones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36)
    private String id;

    @Column(nullable = false)
    private String title;

    private Double tax;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "location_coordinates", columnDefinition = "TEXT")
    @Convert(converter = PolygonConverter.class)
    private List<List<List<Double>>> locationCoordinates;

    @Column(name = "is_active")
    private Boolean isActive;
}
