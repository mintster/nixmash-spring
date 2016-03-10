package com.nixmash.springdata.jpa.dto;

import java.util.Collection;

import javax.persistence.Basic;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.nixmash.springdata.jpa.enums.SignInProvider;
import com.nixmash.springdata.jpa.model.Authority;
import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.jpa.model.validators.ExtendedEmailValidator;

public class SocialUserDTO {

    @Length(min= User.MIN_LENGTH_USERNAME, max=User.MAX_LENGTH_USERNAME)
    private String username = "";

    @Basic
    @ExtendedEmailValidator
    @Length(max=User.MAX_LENGTH_EMAIL_ADDRESS)
    private String email = "";

    private String password = "";

    private SignInProvider signInProvider;
    
    @NotEmpty
    @Length(min=User.MIN_LENGTH_FIRST_NAME, max=User.MAX_LENGTH_FIRST_NAME)
    private String firstName = "";

    @NotEmpty
    @Length(min=User.MIN_LENGTH_LAST_NAME, max=User.MAX_LENGTH_LAST_NAME)
    private String lastName = "";

    private Collection<Authority> authorities;

    public Collection<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<Authority> authorities) {
        this.authorities = authorities;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public SignInProvider getSignInProvider() {
        return signInProvider;
    }

    public void setSignInProvider(SignInProvider signInProvider) {
        this.signInProvider = signInProvider;
    }

    @Override
    public String toString() {
        return "SocialUserDTO{" +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                "lastName='" + lastName + '\'' +
                "signInProvider='" + signInProvider + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
