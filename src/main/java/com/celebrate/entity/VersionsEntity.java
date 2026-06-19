package com.celebrate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "versions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VersionsEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "customer_app_android")
    private String customerAppAndroid;

    @Column(name = "customer_app_ios")
    private String customerAppIos;

    @Column(name = "rider_app_android")
    private String riderAppAndroid;

    @Column(name = "rider_app_ios")
    private String riderAppIos;

    @Column(name = "restaurant_app_android")
    private String restaurantAppAndroid;

    @Column(name = "restaurant_app_ios")
    private String restaurantAppIos;
}
