package com.nixmash.springdata.jpa.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -1402436282978052954L;

    public ResourceNotFoundException() {
        super();
    }

}
