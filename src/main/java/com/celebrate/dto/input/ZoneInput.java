package com.celebrate.dto.input;

import lombok.Data;

import java.util.List;

@Data
public class ZoneInput {
    private String id;
    private String title;
    private String description;
    private List<List<List<Double>>> coordinates;
    public void set_id(String _id){
        this.id = _id;
    }
}
