package com.celebrate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "amount_currency", nullable = false, length = 50)
    private String amountCurrency;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(name = "transaction_id", nullable = false, length = 100)
    private String transactionId;

    @Column(name = "user_type", nullable = false, length = 50)
    private String userType;

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "amount_transferred", nullable = false)
    private Double amountTransferred;

    // Bank details (toBank embedded)
    @Column(name = "bank_account_name", nullable = false)
    private String bankAccountName;

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Column(name = "bank_account_number", nullable = false)
    private String bankAccountNumber;

    @Column(name = "bank_account_code", nullable = false)
    private String bankAccountCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id")
    private RiderEntity rider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private RestaurantEntity store;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
