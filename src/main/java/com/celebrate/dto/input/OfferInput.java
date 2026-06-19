package com.celebrate.dto.input;

import lombok.Data;

import java.util.List;

@Data
public class OfferInput {
    private String id;
    private String name;
    private String tag;
    private List<String> restaurants;
    public void set_id(String _id){
        this.id = _id;
    }
}
