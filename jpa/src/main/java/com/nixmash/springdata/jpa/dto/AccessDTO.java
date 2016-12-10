package com.nixmash.springdata.jpa.dto;

import java.io.Serializable;

/**
 * Created by daveburke on 12/9/16.
 */
public class AccessDTO implements Serializable {

    private static final long serialVersionUID = -5289711184924125135L;

    // region properties

    private boolean isValid;
    private boolean isApproved;
    private String email;
    private String domain;

    // endregion

    // region constructors


    public AccessDTO() {
    }

    public AccessDTO(String email) {
        this.email = email;
        this.isValid = false;
        this.isApproved = false;
    }

    // endregion

    // region getter setters

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    // endregion

    // region utils

    @Override
    public String toString() {
        return "AccessDTO{" +
                "isValid=" + isValid +
                ", isApproved='" + isApproved + '\'' +
                ", email='" + email + '\'' +
                ", domain='" + domain + '\'' +
                '}';
    }

    // endregion

}
