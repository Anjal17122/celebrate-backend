package com.celebrate.dto.response;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BankDetailsResponse {
    private String accountName;
    private String bankName;
    private String accountNumber;
    private String accountCode;
}
