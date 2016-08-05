package com.nixmash.springdata.jpa.dto;

import java.io.Serializable;

/**
 * Created by daveburke on 8/5/16.
 */
public class UserPasswordDTO implements Serializable{

    private static final long serialVersionUID = -2221852531645649922L;

    private String password;
    private String repeatedPassword;
    private String verificationToken;
    private long userId;

    // region Constructors

    public UserPasswordDTO() {
    }

    public UserPasswordDTO(long userId, String verificationToken) {
        this.verificationToken = verificationToken;
        this.userId = userId;
    }

// endregion

    // region Getter/Setters

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }


    // endregion

}
