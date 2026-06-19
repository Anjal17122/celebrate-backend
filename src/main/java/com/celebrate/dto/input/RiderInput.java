package com.celebrate.dto.input;

import lombok.Data;

@Data
public class RiderInput {
    private String id;
    private String name;
    private String email;
    private String username;
    private String password;
    private String phone;
    private String image;
    private Boolean available;
    private String vehicleType;
    private String zone;
    private String accountNumber;
    private BussinessDetailsInput bussinessDetails;
    private LicenseDetailsInput licenseDetails;
    private VehicleDetailsInput vehicleDetails;
    public void set_id(String _id){
        this.id = _id;
    }
}
