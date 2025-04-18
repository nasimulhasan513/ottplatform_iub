package iub.ottplatform_iub.model;

import java.io.Serializable;

public class User implements Serializable {
    private String userId;
    private String username;
    private String email;
    private String password;
    private String userType; // BASIC, PREMIUM, CONTENT_UPLOADER, etc.
    private boolean isActive;

    public User(String userId, String username, String email, String password, String userType) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.isActive = true;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}