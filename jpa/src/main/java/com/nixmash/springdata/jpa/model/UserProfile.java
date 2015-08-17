package com.nixmash.springdata.jpa.model;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * A profile for a system user.
 */
@Entity
@Table(name = "user_profiles")
public class UserProfile {

    private static final long serialVersionUID = 6529028956634326182L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @OneToOne
    @PrimaryKeyJoinColumn
    private User user;

    @Column
    private String address;

    @Column
    private String address2;

    @Column
    private String city;

    @Column
    @Size(min=2)
    private String state;

    @Column
    @Size(min=5, max=10)
    private String zip;

    @Column
    private String phone;

    public UserProfile() {
    }

    public UserProfile(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "UserProfile(" + user.getUsername() + ")";
    }
}
