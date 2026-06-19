package com.celebrate.dto.input;

import lombok.Data;

@Data
public class TaxationInput {
    private String id;
    private Float taxationCharges;
    private Boolean enabled;
    public void set_id(String _id){
        this.id = _id;
    }
}
