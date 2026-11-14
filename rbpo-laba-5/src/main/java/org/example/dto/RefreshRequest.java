package org.example.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;

public class RefreshRequest {

    @NotBlank(message = "refreshToken is required")
    @JsonAlias({"refresh_token", "token"})
    private String refreshToken;

    public RefreshRequest() {
    }

    public RefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        if (refreshToken != null && !refreshToken.isBlank()) {
            return refreshToken.trim();
        }
        return null;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
