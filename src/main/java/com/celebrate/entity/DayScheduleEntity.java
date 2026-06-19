package com.celebrate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "rider_work_schedules")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DayScheduleEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id", nullable = false)
    private RiderEntity rider;

    @Column(nullable = false, length = 20)
    private String day;

    @Column(nullable = false)
    private Boolean enabled;

    @OneToMany(mappedBy = "daySchedule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TimeSlotEntity> slots;
}
