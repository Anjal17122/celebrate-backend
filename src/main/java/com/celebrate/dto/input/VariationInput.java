package com.celebrate.dto.input;

import lombok.Data;

import java.util.List;

@Data
public class VariationInput {
    private String id;
    private String title;
    private Float price;
    private Float discounted;
    private List<String> addons;
    private Boolean isOutOfStock;
    private Integer prepTime;
    public void set_id(String _id){
        this.id = _id;
    }
}
