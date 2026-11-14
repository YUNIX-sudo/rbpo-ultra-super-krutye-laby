package org.example.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @JsonAlias({"username", "login", "user"})
    private String email;

    private String username;

    @NotBlank(message = "Password is required")
    @JsonAlias({"pass"})
    private String password;

    @JsonAlias({"device_id", "device"})
    private String deviceId;

    public LoginRequest() {
    }

    public LoginRequest(String email, String password, String deviceId) {
        this.email = email;
        this.password = password;
        this.deviceId = deviceId;
    }

    public String getEmail() {
        if (email != null && !email.isBlank()) {
            return email.trim();
        }
        if (username != null && !username.isBlank()) {
            return username.trim();
        }
        return null;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        if (username != null && !username.isBlank()) {
            return username.trim();
        }
        if (email != null && !email.isBlank()) {
            return email.trim();
        }
        return null;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceId() {
        if (deviceId != null && !deviceId.isBlank()) {
            return deviceId.trim();
        }
        return null;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
