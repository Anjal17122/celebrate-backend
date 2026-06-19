package com.celebrate.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveNotificationTokenWebResponse {
    private Boolean success;
    private String message;
}
