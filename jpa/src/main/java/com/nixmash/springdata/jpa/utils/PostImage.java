package com.nixmash.springdata.jpa.utils;

import org.springframework.web.multipart.MultipartFile;

public class PostImage {

    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}

