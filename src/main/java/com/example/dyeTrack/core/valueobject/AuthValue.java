package com.example.dyeTrack.core.valueobject;

import com.example.dyeTrack.core.entity.User;

public final class AuthValue {
    private final String token;
    private final User user;

    public AuthValue(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
