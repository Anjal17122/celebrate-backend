package com.celebrate.dto.input;

import lombok.Data;

@Data
public class AddressInput {
    private String id;
    private String longitude;
    private String latitude;
    private String deliveryAddress;
    private String details;
    private String label;
    public void set_id(String _id){
        this.id = _id;
    }
}
