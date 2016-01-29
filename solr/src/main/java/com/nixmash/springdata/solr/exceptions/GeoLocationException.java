
    package com.nixmash.springdata.solr.exceptions;

    public class GeoLocationException extends Exception {

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


