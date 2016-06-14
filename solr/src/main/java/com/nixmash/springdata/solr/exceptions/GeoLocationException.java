
    package com.nixmash.springdata.solr.exceptions;

    public class GeoLocationException extends Exception {

        private static final long serialVersionUID = 3336735270794235096L;
        private String msg;

        public GeoLocationException() {
            super();
        }

        public GeoLocationException(String msg) {
            this.msg = System.currentTimeMillis()
                    + ": " + msg;
        }

        public String getMsg() {
            return msg;
        }


    }


