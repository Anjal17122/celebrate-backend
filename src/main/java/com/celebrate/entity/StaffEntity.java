package com.celebrate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "staff")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36)
    private String id;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(length = 255)
    private String password;

    @Column(name = "plain_password", length = 255)
    private String plainPassword;

    @Column(length = 50)
    private String phone;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "user_type", length = 50)
    private String userType;

    @ElementCollection
    @CollectionTable(name = "staff_permissions", joinColumns = @JoinColumn(name = "staff_id"))
    @Column(name = "permission", length = 100)
    private List<String> permissions;
}
