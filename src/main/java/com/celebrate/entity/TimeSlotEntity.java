package com.celebrate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "rider_time_slots")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private DayScheduleEntity daySchedule;

    @Column(name = "start_time", nullable = false, length = 50)
    private String startTime;

    @Column(name = "end_time", nullable = false, length = 50)
    private String endTime;
}
