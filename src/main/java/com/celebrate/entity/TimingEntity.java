package com.celebrate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "restaurant_timings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimingEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opening_time_id", nullable = false)
    private OpeningTimeEntity openingTime;

    @Column(name = "start_time", length = 50)
    private String startTime;

    @Column(name = "end_time", length = 50)
    private String endTime;
}
