package org.example.service;

import io.jsonwebtoken.Claims;
import org.example.dto.TokenResponse;
import org.example.exception.BadRequestException;
import org.example.exception.UnauthorizedException;
import org.example.model.SessionStatus;
import org.example.model.User;
import org.example.model.UserSession;
import org.example.repository.UserRepository;
import org.example.repository.UserSessionRepository;
import org.example.securityandauth.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class UserSessionService {

    private final UserSessionRepository userSessionRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserSessionService(UserSessionRepository userSessionRepository,
                              UserRepository userRepository,
                              JwtService jwtService) {
        this.userSessionRepository = userSessionRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public TokenResponse issueTokenPair(User user, String deviceId) {
        Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant accessExpiry = now.plusSeconds(jwtService.getAccessExpirationSeconds());
        Instant refreshExpiry = now.plusSeconds(jwtService.getRefreshExpirationSeconds());

        String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getRole(), now, accessExpiry);
        String refreshToken = jwtService.generateRefreshToken(user.getEmail(), now, refreshExpiry);

        String normalizedDeviceId = (deviceId != null && !deviceId.isBlank()) ? deviceId.trim() : null;

        UserSession session = new UserSession();
        session.setUsername(user.getEmail());
        session.setDeviceId(normalizedDeviceId);
        session.setAccessToken(accessToken);
        session.setRefreshToken(refreshToken);
        session.setAccessTokenExpiry(accessExpiry);
        session.setRefreshTokenExpiry(refreshExpiry);
        session.setStatus(SessionStatus.ACTIVE);

        userSessionRepository.save(session);
        return new TokenResponse(accessToken, refreshToken);
    }

    @Transactional(noRollbackFor = {UnauthorizedException.class})
    public TokenResponse refreshTokenPair(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BadRequestException("refreshToken is required");
        }

        refreshToken = refreshToken.trim();

        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        Claims claims;
        try {
            claims = jwtService.parseClaims(refreshToken);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        String sub = claims.getSubject();

        UserSession session = userSessionRepository.findByRefreshTokenForUpdate(refreshToken)
                .orElseThrow(() -> new UnauthorizedException("Session not found"));

        if (session.getStatus() == SessionStatus.REVOKED) {
            throw new UnauthorizedException("Session is revoked");
        }

        if (session.getStatus() == SessionStatus.USED) {
            if (session.getRefreshTokenExpiry().isAfter(Instant.now())) {
                session.setStatus(SessionStatus.REVOKED);
                userSessionRepository.saveAndFlush(session);
            }
            throw new UnauthorizedException("Session has already been used");
        }

        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new UnauthorizedException("Session is not active");
        }

        if (session.getRefreshTokenExpiry().isBefore(Instant.now())) {
            throw new UnauthorizedException("Session has expired");
        }

        if (sub == null || !sub.equals(session.getUsername())) {
            throw new UnauthorizedException("Token subject does not match session owner");
        }

        User user = userRepository.findByEmail(session.getUsername())
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        if (user.getIsActive() == null || user.getIsActive() != 1 || "DEACTIVATED".equalsIgnoreCase(user.getRole())) {
            throw new UnauthorizedException("User is deactivated");
        }

        session.setStatus(SessionStatus.USED);
        userSessionRepository.save(session);

        Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant accessExpiry = now.plusSeconds(jwtService.getAccessExpirationSeconds());
        Instant refreshExpiry = now.plusSeconds(jwtService.getRefreshExpirationSeconds());

        String newAccessToken = jwtService.generateAccessToken(user.getEmail(), user.getRole(), now, accessExpiry);
        String newRefreshToken = jwtService.generateRefreshToken(user.getEmail(), now, refreshExpiry);

        UserSession newSession = new UserSession();
        newSession.setUsername(session.getUsername());
        newSession.setDeviceId(session.getDeviceId());
        newSession.setAccessToken(newAccessToken);
        newSession.setRefreshToken(newRefreshToken);
        newSession.setAccessTokenExpiry(accessExpiry);
        newSession.setRefreshTokenExpiry(refreshExpiry);
        newSession.setStatus(SessionStatus.ACTIVE);

        userSessionRepository.save(newSession);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}
