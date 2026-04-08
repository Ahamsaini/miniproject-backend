package com.mainApp.responcedto;

import lombok.Data;
import java.time.LocalDateTime;
import com.mainApp.roles.SessionCodeType;

@Data
public class SessionCodeResponse {
    private String code;
    private SessionCodeType type;
    private LocalDateTime generatedAt;
    private LocalDateTime expiresAt;
    private Boolean isValid;
}
