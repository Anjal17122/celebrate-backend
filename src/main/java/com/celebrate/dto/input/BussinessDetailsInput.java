package com.celebrate.dto.input;

import lombok.Data;

@Data
public class BussinessDetailsInput {
    private String bankName;
    private String accountName;
    private String accountCode;
    private String accountNumber;
    private String bussinessRegNo;
    private String companyRegNo;
    private Double taxRate;
}
