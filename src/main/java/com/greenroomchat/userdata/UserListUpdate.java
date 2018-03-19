package com.greenroomchat.userdata;

import java.io.Serializable;

public class UserListUpdate implements Serializable {
    private boolean connecting;
    private String username;

    public boolean isConnecting() {
        return connecting;
    }

    public void setConnecting(boolean connecting) {
        this.connecting = connecting;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserListUpdate(boolean connecting, String username){
        this.connecting = connecting;
        this.username = username;
    }
}
