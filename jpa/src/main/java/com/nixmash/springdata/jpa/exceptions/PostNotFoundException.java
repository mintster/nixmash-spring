package com.nixmash.springdata.jpa.exceptions;

/**
 * Created by daveburke on 6/15/16.
 */
public class PostNotFoundException extends Exception {

    private static final long serialVersionUID = 3613978896775863909L;

    private String msg;

    public PostNotFoundException() {
        super();
    }

    public PostNotFoundException(String msg) {
        this.msg = System.currentTimeMillis()
                + ": " + msg;
    }

    public String getMsg() {
        return msg;
    }


}
