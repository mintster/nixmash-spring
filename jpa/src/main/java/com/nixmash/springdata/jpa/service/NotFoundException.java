package com.nixmash.springdata.jpa.service;

/**
 * This exception is thrown if the requested contact is not found.
 * @author Petri Kainulainen
 */
public class NotFoundException extends Exception {

    public NotFoundException(String message) {
        super(message);
    }
}
