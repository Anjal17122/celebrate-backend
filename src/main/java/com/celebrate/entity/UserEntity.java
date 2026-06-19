package com.celebrate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 50)
    private String phone;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "user_type", length = 50)
    private String userType;

    @Column(name = "phone_is_verified")
    private Boolean phoneIsVerified;

    @Column(name = "email_is_verified")
    private Boolean emailIsVerified;

    @Column(length = 255)
    private String password;

    @Column(length = 50)
    private String status;

    @Column(name = "status_reason", columnDefinition = "TEXT")
    private String statusReason;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "is_order_notification")
    private Boolean isOrderNotification;

    @Column(name = "is_offer_notification")
    private Boolean isOfferNotification;

    @Column(name = "notification_token", columnDefinition = "TEXT")
    private String notificationToken;

    @Column(name = "apple_id")
    private String appleId;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AddressEntity> addresses;

    @ElementCollection
    @CollectionTable(name = "user_favourites", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "restaurant_id", length = 36)
    private List<String> favourite;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
