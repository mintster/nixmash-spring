
    package com.nixmash.springdata.jpa.exceptions;

    public class ContactNotFoundException extends Exception {


        private String msg;

        public ContactNotFoundException() {
            super();
        }

        public ContactNotFoundException(String msg) {
            this.msg = System.currentTimeMillis()
                    + ": " + msg;
        }

        public String getMsg() {
            return msg;
        }


    }


