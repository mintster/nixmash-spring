package com.nixmash.springdata.jpa.dto;

import org.springframework.web.multipart.MultipartFile;

public class ProfileImageDTO {

    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}

