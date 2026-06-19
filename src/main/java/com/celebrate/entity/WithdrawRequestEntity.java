package com.celebrate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "withdraw_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawRequestEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "request_id", nullable = false, length = 100)
    private String requestId;

    @Column(name = "request_amount", nullable = false)
    private Double requestAmount;

    @Column(name = "request_time", nullable = false)
    private String requestTime;

    @Column(nullable = false, length = 50)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id")
    private RiderEntity rider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private RestaurantEntity store;

    @Column(name = "amount_transferred")
    private Double amountTransferred;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
