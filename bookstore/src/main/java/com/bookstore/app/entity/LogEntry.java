package com.bookstore.app.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("log_entry")
public class LogEntry {
    @Id
    @Column("log_id")
    private Long logId;

    @Column("timestamp")
    private LocalDateTime timestamp;

    @Column("method")
    private String method;

    @Column("endpoint")
    private String endpoint;

    @Column("request_body")
    private String requestBody;

    @Column("response_body")
    private String responseBody;

    @Column("status_code")
    private Integer statusCode;

    @Column("ip_address")
    private String ipAddress;

    @Column("user_id")
    private Long userId;
}  