package com.celebrate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "owners")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OwnerEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36)
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "unique_id", length = 100)
    private String uniqueId;

    @Column(unique = true, length = 255)
    private String email;

    private String name;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(length = 500)
    private String image;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @Column(length = 255)
    private String password;

    @Column(name = "plain_password", length = 255)
    private String plainPassword;

    @Column(name = "user_type", nullable = false, length = 50)
    private String userType;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "push_token", columnDefinition = "TEXT")
    private String pushToken;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<RestaurantEntity> restaurants;
}
