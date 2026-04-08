package com.mainApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mainApp.model.SessionCode;

import com.mainApp.roles.SessionCodeType;
import java.util.List;

@Repository
public interface SessionCodeRepository extends JpaRepository<SessionCode, String> {
    Optional<SessionCode> findByCode(String code);

    List<SessionCode> findByLabSessionId(String labSessionId);

    Optional<SessionCode> findByLabSessionIdAndType(String labSessionId, SessionCodeType type);
}
