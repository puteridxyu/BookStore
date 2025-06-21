package com.bookstore.app.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("log_entry")
public class LogEntry {
    @Id
    private Long logId;
    private LocalDateTime timestamp;
    private String method;
    private String endpoint;
    private String requestBody;
    private String responseBody;
    private Integer statusCode;
    private String ipAddress;
    private Long userId;
}