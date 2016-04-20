package com.nixmash.springdata.jpa.model;

import com.nixmash.springdata.jpa.enums.Role;
import com.nixmash.springdata.jpa.enums.SignInProvider;
import com.nixmash.springdata.jpa.model.validators.ExtendedEmailValidator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

@Entity
@Table(name = "users")
public class User implements UserDetails, Serializable {

    private static final long serialVersionUID = 2002390446280945447L;
    private static final Logger logger = LoggerFactory.getLogger(User.class);

    public static final int MAX_LENGTH_EMAIL_ADDRESS = 100;
    public static final int MAX_LENGTH_FIRST_NAME = 25;
    public static final int MAX_LENGTH_LAST_NAME = 50;
    public static final int MAX_LENGTH_USERNAME = 15;
    public static final int MAX_LENGTH_PASSWORD = 20;
    public static final int MAX_LENGTH_PROVIDERID = 25;

    public static final int MIN_LENGTH_USERNAME = 3;
    public static final int MIN_LENGTH_PASSWORD = 6;
    public static final int MIN_LENGTH_FIRST_NAME = 2;
    public static final int MIN_LENGTH_LAST_NAME = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    protected Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Transient
    public boolean isNew() {
        return (this.id == null);
    }

    @Column(unique = true)
    @NotEmpty
    @Length(min=MIN_LENGTH_USERNAME, max=MAX_LENGTH_USERNAME)
    private String username;

    @Column
    @NotEmpty
    @Length(min=MIN_LENGTH_PASSWORD)
    private String password;

    @Basic
    @ExtendedEmailValidator
    @NotEmpty
    @Length(max=MAX_LENGTH_EMAIL_ADDRESS)
    @Column(unique=true, nullable = false)
    private String email;

    @Column(name = "first_name")
    @NotEmpty
    @Length(max=MAX_LENGTH_FIRST_NAME)
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty
    @Length(max=MAX_LENGTH_LAST_NAME)
    private String lastName;

    @Column(name = "account_expired")
    private boolean accountExpired = false;

    @Column(name = "account_locked")
    private boolean accountLocked = false;

    @Column(name = "credentials_expired")
    private boolean credentialsExpired = false;

    @Column(name = "provider_id", length =25)
    @Enumerated(EnumType.STRING)
    private SignInProvider signInProvider;

    @Column(name = "user_key", length =25)
    private String userKey;

    @Column(name = "has_avatar")
    private boolean hasAvatar = false;

    @Column
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_authorities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    public Collection<Authority> authorities;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile userProfile;

    public User() {
        this.authorities = new LinkedHashSet<>();
    }

    public User(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public SignInProvider getSignInProvider() {
        return signInProvider;
    }

    public void setSignInProvider(SignInProvider signInProvider) {
        this.signInProvider = signInProvider;
    }

    @Override
    public Collection<Authority> getAuthorities() {
        return authorities;
    }


    public boolean hasAuthority(Role role) {
        return hasAuthority(String.valueOf(role));
    }

    private boolean hasAuthority(String targetAuthority) {
        if (targetAuthority == null) {
            return false;
        }
        if (authorities == null) {
            logger.info("authorities is null for user " + this);
        }

        for (Authority authority : authorities) {
            if (targetAuthority.equals(authority.getAuthority())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public boolean hasAvatar() {
        return hasAvatar;
    }

    public void setHasAvatar(boolean hasAvatar) {
        this.hasAvatar = hasAvatar;
    }

// @formatter:off
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username=" + username +
                ", email=" + email +
                ", firstName=" + firstName  +
                ", lastName=" + lastName  +
                ", accountExpired=" + accountExpired +
                ", accountLocked=" + accountLocked +
                ", credentialsExpired=" + credentialsExpired +
                ", userKey=" + userKey +
                ", hasAvatar=" + hasAvatar +
                ", signInProvider=" + signInProvider +
                ", enabled=" + enabled +
                ", new=" + this.isNew() +
                '}';
    }

    public void update(String username, String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
    }

    // @formatter:on
    
}
