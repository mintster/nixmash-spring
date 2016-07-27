package com.nixmash.springdata.jpa.dto;

import java.io.Serializable;

/**
 * Created by daveburke on 7/26/16.
 */
public class AlphabetDTO implements Serializable {
    private static final long serialVersionUID = -8261176350405087033L;

    private String alphaCharacter;
    private Boolean isActive;

    public AlphabetDTO() {}

    public AlphabetDTO(String alphaCharacter, Boolean isActive) {
        this.alphaCharacter = alphaCharacter;
        this.isActive = isActive;
    }

    public String getAlphaCharacter() {
        return alphaCharacter;
    }

    public void setAlphaCharacter(String alphaCharacter) {
        this.alphaCharacter = alphaCharacter;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
