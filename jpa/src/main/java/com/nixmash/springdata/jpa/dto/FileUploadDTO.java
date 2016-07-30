package com.nixmash.springdata.jpa.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by daveburke on 7/29/16.
 */
public class FileUploadDTO {

    private String filename;
    private Long parentId;
    private List<MultipartFile> files;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> fileData) {
        this.files = fileData;
    }
}
