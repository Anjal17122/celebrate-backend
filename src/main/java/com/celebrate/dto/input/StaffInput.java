package com.celebrate.dto.input;

import lombok.Data;

import java.util.List;

@Data
public class StaffInput {
    private String id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private Boolean isActive;
    private String userType;
    private List<String> permissions;
    public void set_id(String _id){
        this.id = _id;
    }
}
