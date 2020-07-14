package com.tptien.tpractice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("response")
    @Expose
    private String Response;
    @SerializedName("iduser")
    @Expose
    private String idUser;
    @SerializedName("username")
    @Expose
    private String userName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("displayname")
    @Expose
    private String displayName;
    @SerializedName("role")
    @Expose
    private String role;

    public User(String userName, String displayName) {
        this.userName = userName;
        this.displayName = displayName;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getResponse() {
        return Response;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getRole() {
        return role;
    }
}
