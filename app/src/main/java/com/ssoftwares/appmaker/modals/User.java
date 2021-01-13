package com.ssoftwares.appmaker.modals;

public class User {
    String id;
    String username;
    String email;
    String provider;
    boolean confirmed;
    boolean blocked;
    String phone;

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getProvider() {
        return provider;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public String getPhone() {
        return phone;
    }
}
