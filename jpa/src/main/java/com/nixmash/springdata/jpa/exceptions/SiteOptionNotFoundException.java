
    package com.nixmash.springdata.jpa.exceptions;

    public class SiteOptionNotFoundException extends Exception {


        private String msg;

        public SiteOptionNotFoundException() {
            super();
        }

        public SiteOptionNotFoundException(String msg) {
            this.msg = System.currentTimeMillis()
                    + ": " + msg;
        }

        public String getMsg() {
            return msg;
        }


    }


