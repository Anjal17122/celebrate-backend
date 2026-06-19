package com.celebrate.dto.response;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RiderWithdrawHistoryResponse {
    private String id;
    private String status;
    private Double amountTransferred;
    private String createdAt;
    public String get_id() { return id; }
}
