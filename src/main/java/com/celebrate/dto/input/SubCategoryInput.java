package com.celebrate.dto.input;

import lombok.Data;

@Data
public class SubCategoryInput {
    private String id;
    private String title;
    private String parentCategoryId;
    public void set_id(String _id){
        this.id = _id;
    }
}
