package org.example.repository;

import jakarta.persistence.LockModeType;
import org.example.model.SessionStatus;
import org.example.model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {

    Optional<UserSession> findByRefreshToken(String refreshToken);

    Optional<UserSession> findByRefreshTokenAndStatus(String refreshToken, SessionStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM UserSession s WHERE s.refreshToken = :refreshToken")
    Optional<UserSession> findByRefreshTokenForUpdate(@Param("refreshToken") String refreshToken);
}
