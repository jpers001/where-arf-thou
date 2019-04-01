package edu.odu.cs.air411.wherearfthou;

import com.google.gson.annotations.SerializedName;

public class User
{
    @SerializedName("Login")
    private String Login;

    @SerializedName("Welcome")
    private String Welcome;

    @SerializedName("registration")
    private String registration;

    public String getLogin() {
        return Login;
    }

    public String getWelcome() {
        return Welcome;
    }
    public String getRegistration(){
        return registration;
    }
}