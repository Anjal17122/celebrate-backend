package com.celebrate.dto.input;

import lombok.Data;

import java.util.List;

@Data
public class TippingInput {
    private String id;
    private List<Float> tipVariations;
    private Boolean enabled;
    public void set_id(String _id){
        this.id = _id;
    }
}
