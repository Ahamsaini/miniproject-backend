package com.mainApp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import com.mainApp.roles.SessionCodeType;

@Data
@Entity
@Table(name = "session_codes", uniqueConstraints = @UniqueConstraint(columnNames = { "lab_session_id", "code_type" }))
public class SessionCode extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_session_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({ "sessionCodes", "attendances", "labAllocations" })
    private LabSession labSession;

    @Enumerated(EnumType.STRING)
    @Column(name = "code_type", nullable = false)
    private SessionCodeType type;

    @Column(name = "code", nullable = false, unique = true, length = 10)
    private String code;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "is_used")
    private Boolean isUsed = false;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @Column(name = "total_uses")
    private Integer totalUses = 0;

    @Column(name = "max_uses")
    private Integer maxUses = 50; // Maximum students who can use this code

    @Column(name = "is_valid")
    private Boolean isValid = true;
}
