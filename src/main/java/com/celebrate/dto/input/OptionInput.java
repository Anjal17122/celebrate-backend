package com.celebrate.dto.input;

import lombok.Data;

@Data
public class OptionInput {
    private String id;
    private String title;
    private String description;
    private Float price;
    public void set_id(String _id){
        this.id = _id;
    }
}
