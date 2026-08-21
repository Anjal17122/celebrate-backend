package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BussinessDetailsResponse {
    private String bankName;
    private String accountName;
    private String accountCode;
    private String accountNumber;
    private String bussinessRegNo;
    private String companyRegNo;
    private Double taxRate;
}
