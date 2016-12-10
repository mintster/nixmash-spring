package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.dto.AccessDTO;

/**
 * Created by daveburke on 12/10/16.
 */
public interface AccessService {

    boolean isEmailApproved(String email);

    AccessDTO createAccessDTO(String email);
}
