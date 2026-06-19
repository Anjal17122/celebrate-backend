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
    private Double accountNumber;
    private Double bussinessRegNo;
    private Double companyRegNo;
    private Double taxRate;
}
