package com.celebrate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "riders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiderEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36)
    private String id;

    @Column(nullable = false)
    private String name;

    private String email;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 50)
    private String phone;

    @Column(length = 500)
    private String image;

    @Column(nullable = false)
    private Boolean available;

    @Column(name = "time_zone", length = 100)
    private String timeZone;

    @Column(name = "vehicle_type", length = 100)
    private String vehicleType;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "account_number", length = 100)
    private String accountNumber;

    @Column(name = "current_wallet_amount")
    private Double currentWalletAmount;

    @Column(name = "total_wallet_amount")
    private Double totalWalletAmount;

    @Column(name = "withdrawn_wallet_amount")
    private Double withdrawnWalletAmount;

    @Column(name = "lat", length = 100)
    private String lat;

    @Column(name = "lng", length = 100)
    private String lng;

    @Column(name = "notification_token", columnDefinition = "TEXT")
    private String notificationToken;

    // BussinessDetails
    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_code")
    private String accountCode;

    @Column(name = "bussiness_reg_no")
    private String bussinessRegNo;

    @Column(name = "company_reg_no")
    private String companyRegNo;

    @Column(name = "tax_rate")
    private Double taxRate;

    // LicenseDetails
    @Column(name = "license_number")
    private String licenseNumber;

    @Column(name = "license_expiry_date")
    private String licenseExpiryDate;

    @Column(name = "license_image", length = 500)
    private String licenseImage;

    // VehicleDetails
    @Column(name = "vehicle_number")
    private String vehicleNumber;

    @Column(name = "vehicle_image", length = 500)
    private String vehicleImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private ZoneEntity zone;

    @OneToMany(mappedBy = "rider", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DayScheduleEntity> workSchedule;

    @ElementCollection
    @CollectionTable(name = "rider_assigned_orders", joinColumns = @JoinColumn(name = "rider_id"))
    @Column(name = "order_id", length = 36)
    private List<String> assigned;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
