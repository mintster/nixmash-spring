package com.nixmash.springdata.jpa.dto;

import com.nixmash.springdata.jpa.model.Authority;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by daveburke on 4/12/16.
 */
public class RoleDTO {

    private Long id;

    @NotEmpty
    @Length(min = Authority.MIN_LENGTH_AUTHORITY, max = Authority.MAX_LENGTH_AUTHORITY)
    private String authority;

    public RoleDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
