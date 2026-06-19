package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LicenseDetailsResponse {
    private String number;
    private String expiryDate;
    private String image;
}
