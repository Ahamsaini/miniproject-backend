package com.mainApp.mapper;

import org.springframework.stereotype.Component;

import com.mainApp.model.SessionCode;
import com.mainApp.responcedto.SessionCodeResponse;

@Component
public class SessionCodeMapper {

    public SessionCodeResponse toResponse(SessionCode sessionCode) {
        if (sessionCode == null) {
            return null;
        }

        SessionCodeResponse response = new SessionCodeResponse();
        response.setCode(sessionCode.getCode());
        response.setType(sessionCode.getType());
        response.setGeneratedAt(sessionCode.getGeneratedAt());

        response.setExpiresAt(sessionCode.getExpiresAt());
        response.setIsValid(sessionCode.getIsValid());

        return response;
    }
}

