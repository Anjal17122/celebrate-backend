package com.celebrate.dto.input;

import lombok.Data;

import java.util.List;

@Data
public class FoodInput {
    private String id;
    private String restaurant;
    private String category;
    private String subCategory;
    private String title;
    private String description;
    private String image;
    private List<VariationInput> variations;
    private Boolean isActive;
    private Boolean isOutOfStock;

    public void set_id(String _id){
        this.id = _id;
    }
}
