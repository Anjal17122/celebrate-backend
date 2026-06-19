package com.celebrate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36)
    private String id;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private OwnerEntity admin;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String action;

    @Column(name = "target_type", length = 100)
    private String targetType;

    @Column(name = "target_id", length = 36)
    private String targetId;

    @Column(columnDefinition = "TEXT")
    private String changes;
}
