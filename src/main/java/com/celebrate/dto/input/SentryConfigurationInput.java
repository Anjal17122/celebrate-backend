package com.celebrate.dto.input;

import lombok.Data;

@Data
public class SentryConfigurationInput {
    private String dashboardSentryUrl;
    private String webSentryUrl;
    private String apiSentryUrl;
    private String customerAppSentryUrl;
    private String restaurantAppSentryUrl;
    private String riderAppSentryUrl;
}
