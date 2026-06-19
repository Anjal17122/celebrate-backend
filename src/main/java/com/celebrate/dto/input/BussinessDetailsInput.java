package com.celebrate.dto.input;

import lombok.Data;

@Data
public class BussinessDetailsInput {
    private String bankName;
    private String accountName;
    private String accountCode;
    private Double accountNumber;
    private Double bussinessRegNo;
    private Double companyRegNo;
    private Double taxRate;
}
