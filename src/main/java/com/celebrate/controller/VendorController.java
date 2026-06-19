package com.celebrate.controller;

import com.celebrate.dto.input.StaffInput;
import com.celebrate.dto.input.VendorInput;
import com.celebrate.dto.response.OwnerResponse;
import com.celebrate.dto.response.StaffResponse;
import com.celebrate.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @QueryMapping
    public List<OwnerResponse> vendors() {
        return vendorService.getVendors();
    }

    @QueryMapping
    public OwnerResponse getVendor(@Argument String id) {
        return vendorService.getVendor(id);
    }

    @QueryMapping
    public OwnerResponse restaurantByOwner(@Argument String id) {
        return vendorService.restaurantByOwner(id);
    }

    @MutationMapping
    public OwnerResponse createVendor(@Argument VendorInput vendorInput) {
        return vendorService.createVendor(vendorInput);
    }

    @MutationMapping
    public OwnerResponse editVendor(@Argument VendorInput vendorInput) {
        return vendorService.editVendor(vendorInput);
    }

    @MutationMapping
    public boolean deleteVendor(@Argument String id) {
        return vendorService.deleteVendor(id);
    }

    @MutationMapping
    public OwnerResponse uploadToken(@Argument String id, @Argument String pushToken) {
        return vendorService.uploadToken(id, pushToken);
    }

    // Staff
    @QueryMapping
    public List<StaffResponse> staffs() {
        return vendorService.getStaffs();
    }

    @QueryMapping
    public StaffResponse staff(@Argument String id) {
        return vendorService.getStaff(id);
    }

    @MutationMapping
    public StaffResponse createStaff(@Argument StaffInput staffInput) {
        return vendorService.createStaff(staffInput);
    }

    @MutationMapping
    public StaffResponse editStaff(@Argument StaffInput staffInput) {
        return vendorService.editStaff(staffInput);
    }

    @MutationMapping
    public StaffResponse deleteStaff(@Argument String id) {
        return vendorService.deleteStaff(id);
    }
}
