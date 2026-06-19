package com.celebrate.dto.input;

import lombok.Data;

import java.util.List;

@Data
public class CreateAddonInput {
    private String id;
    private String title;
    private String description;
    private List<String> options;
    private Integer quantityMinimum;
    private Integer quantityMaximum;
    public void set_id(String _id){
        this.id = _id;
    }
}
