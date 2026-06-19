package com.celebrate.dto.input;

import lombok.Data;

@Data
public class CuisineInput {
    private String id;
    private String name;
    private String description;
    private String image;
    private String shopType;
    public void set_id(String _id){
        this.id = _id;
    }
}
