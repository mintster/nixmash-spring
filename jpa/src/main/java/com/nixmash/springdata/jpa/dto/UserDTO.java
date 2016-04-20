package com.nixmash.springdata.jpa.dto;

import com.nixmash.springdata.jpa.enums.SignInProvider;
import com.nixmash.springdata.jpa.model.Authority;
import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.jpa.model.validators.ExtendedEmailValidator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Basic;
import java.util.Collection;

public class UserDTO {

    public UserDTO() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private Long userId;
    private boolean updateChildren = true;

    @Length(min = User.MIN_LENGTH_USERNAME, max = User.MAX_LENGTH_USERNAME)
    private String username = "";

    @Basic
    @ExtendedEmailValidator
    @Length(max = User.MAX_LENGTH_EMAIL_ADDRESS)
    private String email = "";

    //    @Length(min=User.MIN_LENGTH_PASSWORD, max=User.MAX_LENGTH_PASSWORD)
    private String password = "";

    @NotEmpty
    @Length(min = User.MIN_LENGTH_FIRST_NAME, max = User.MAX_LENGTH_FIRST_NAME)
    private String firstName = "";

    @NotEmpty
    @Length(min = User.MIN_LENGTH_LAST_NAME, max = User.MAX_LENGTH_LAST_NAME)
    private String lastName = "";

    private boolean hasAvatar;
    private String userKey;

    private SignInProvider signInProvider;

    private String repeatedPassword = "";

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
        return "something";
        //return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatedPassword() {
        return "something";
        //  return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
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

    public boolean isHasAvatar() {
        return hasAvatar;
    }

    public void setHasAvatar(boolean hasAvatar) {
        this.hasAvatar = hasAvatar;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public boolean isNew() {
        return (this.userId == null);
    }

    public boolean isUpdateChildren() {
        return updateChildren;
    }

    public void setUpdateChildren(boolean updateChildren) {
        this.updateChildren = updateChildren;
    }

    @Override
    public String toString() {
        return "UserCreateForm{" +
                ", username=" + username  +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", email=" + email +
                '}';
    }
}
