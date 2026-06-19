package com.celebrate.dto.input;

import lombok.Data;

@Data
public class BannerInput {
    private String id;
    private String title;
    private String description;
    private String file;
    private String action;
    private String screen;
    private String parameters;
    public void set_id(String _id){
        this.id = _id;
    }
}
