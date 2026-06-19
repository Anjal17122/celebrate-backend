package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiderResponse {
    private String id;
    private String name;
    private String email;
    private String username;
    private String password;
    private String phone;
    private String image;
    private Boolean available;
    private ZoneResponse zone;
    private String timeZone;
    private String vehicleType;
    private Boolean isActive;
    private String createdAt;
    private String updatedAt;
    private List<String> assigned;
    private PointResponse location;
    private String accountNumber;
    private Double currentWalletAmount;
    private Double totalWalletAmount;
    private Double withdrawnWalletAmount;
    private BussinessDetailsResponse bussinessDetails;
    private LicenseDetailsResponse licenseDetails;
    private VehicleDetailsResponse vehicleDetails;
    private List<DayScheduleResponse> workSchedule;

    public String get_id() { return id; }
}
