package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormSubmissionResponse {
    private String message;
    private String status;
}
