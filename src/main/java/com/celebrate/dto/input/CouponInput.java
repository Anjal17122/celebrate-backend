package com.celebrate.dto.input;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CouponInput {
    private String id;
    private String title;
    private Double discount;
    private Boolean enabled;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean lifeTimeActive;
    public void set_id(String _id){
        this.id = _id;
    }
}
