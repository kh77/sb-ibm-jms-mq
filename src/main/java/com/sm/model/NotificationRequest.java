package com.sm.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NotificationRequest {
    private String message;
    private String identifier;
}
