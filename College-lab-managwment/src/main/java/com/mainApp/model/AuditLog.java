package com.mainApp.model;



import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "audit_logs")
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(name = "action", nullable = false, length = 50)
    private String action; // CREATE, UPDATE, DELETE, LOGIN, LOGOUT
    
    @Column(name = "entity_name", length = 100)
    private String entityName;
    
    @Column(name = "entity_id", length = 100)
    private String entityId;
    
    @Column(name = "old_values", columnDefinition = "JSON")
    private String oldValues;
    
    @Column(name = "new_values", columnDefinition = "JSON")
    private String newValues;
    
    @Column(name = "performed_by")
    private String performedBy; // User ID
    
    @Column(name = "performed_by_username")
    private String performedByUsername;
    
    @Column(name = "ip_address", length = 45)
    private String ipAddress;
    
    @Column(name = "user_agent", length = 500)
    private String userAgent;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    @Column(name = "status", length = 20)
    private String status; // SUCCESS, FAILED
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(name = "request_url", length = 500)
    private String requestUrl;
    
    @Column(name = "request_method", length = 10)
    private String requestMethod;
    
    @Column(name = "execution_time_ms")
    private Long executionTimeMs;
}
