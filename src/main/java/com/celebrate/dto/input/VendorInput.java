package com.celebrate.dto.input;

import lombok.Data;

@Data
public class VendorInput {
    private String id;
    private String email;
    private String password;
    private String name;
    private String image;
    private String phoneNumber;
    private String lastName;
    private String firstName;
    public void set_id(String _id){
        this.id = _id;
    }
}
