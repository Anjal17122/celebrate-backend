package com.celebrate.dto.input;

import lombok.Data;

@Data
public class UpdateShopTypeInput {
    private String id;
    private String image;
    private String name;
    private Boolean isActive;
    public void set_id(String _id){
        this.id = _id;
    }
}
