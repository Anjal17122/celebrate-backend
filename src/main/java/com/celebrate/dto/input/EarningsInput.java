package com.celebrate.dto.input;

import lombok.Data;

@Data
public class EarningsInput {
    private String id;
    private String rider;
    private String orderId;
    private Double deliveryFee;
    private String orderStatus;
    private String paymentMethod;
    private String deliveryTime;
    public void set_id(String _id){
        this.id = _id;
    }
}
