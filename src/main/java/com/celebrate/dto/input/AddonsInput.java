package com.celebrate.dto.input;

import lombok.Data;

import java.util.List;

@Data
public class AddonsInput {
    private String id;
    private List<String> options;
    public void set_id(String _id){
        this.id = _id;
    }
}
