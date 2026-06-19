package com.celebrate.controller;

import com.celebrate.dto.input.*;
import com.celebrate.dto.response.RiderResponse;
import com.celebrate.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RiderController {

    private final RiderService riderService;

    @QueryMapping
    public List<RiderResponse> riders() {
        return riderService.getAllRiders();
    }

    @QueryMapping
    public RiderResponse rider(@Argument String id) {
        return riderService.getRider(id);
    }

    @QueryMapping
    public List<RiderResponse> availableRiders() {
        return riderService.getAvailableRiders();
    }

    @QueryMapping
    public List<RiderResponse> ridersByZone(@Argument String id) {
        return riderService.getRidersByZone(id);
    }

    @MutationMapping
    public RiderResponse createRider(@Argument RiderInput riderInput) {
        return riderService.createRider(riderInput);
    }

    @MutationMapping
    public RiderResponse editRider(@Argument RiderInput riderInput) {
        return riderService.editRider(riderInput);
    }

    @MutationMapping
    public RiderResponse deleteRider(@Argument String id) {
        return riderService.deleteRider(id);
    }

    // Schema has typo: toggleAvailablity (missing 'i')
    @MutationMapping("toggleAvailablity")
    public RiderResponse toggleAvailablity(@Argument String id) {
        return riderService.toggleAvailability(id);
    }

    @MutationMapping
    public RiderResponse updateRiderLocation(@Argument String latitude, @Argument String longitude) {
        return riderService.updateRiderLocation(latitude, longitude);
    }

    @MutationMapping
    public RiderResponse updateWorkSchedule(
            @Argument String riderId,
            @Argument List<DayScheduleInput> workSchedule,
            @Argument String timeZone) {
        return riderService.updateWorkSchedule(riderId, workSchedule, timeZone);
    }

    @MutationMapping
    public RiderResponse updateRiderBussinessDetails(@Argument String id, @Argument BussinessDetailsInput bussinessDetails) {
        return riderService.updateRiderBussinessDetails(id, bussinessDetails);
    }

    @MutationMapping
    public RiderResponse updateRiderLicenseDetails(@Argument String id, @Argument LicenseDetailsInput licenseDetails) {
        return riderService.updateRiderLicenseDetails(id, licenseDetails);
    }

    @MutationMapping
    public RiderResponse updateRiderVehicleDetails(@Argument String id, @Argument VehicleDetailsInput vehicleDetails) {
        return riderService.updateRiderVehicleDetails(id, vehicleDetails);
    }

    // Schema: notifyRiders(id: String!)
    @MutationMapping
    public boolean notifyRiders(@Argument String id) {
        return riderService.notifyRiders(id);
    }
}
