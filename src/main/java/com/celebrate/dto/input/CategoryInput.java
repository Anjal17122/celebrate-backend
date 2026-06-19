package com.celebrate.dto.input;

import lombok.Data;

import java.util.List;

@Data
public class CategoryInput {
    private String id;
    private String title;
    private String image;
    private String restaurant;
    private List<SubCategoryInput> subCategories;
    public void set_id(String _id){
        this.id = _id;
    }
}
