package com.nixmash.springdata.jpa.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileUploadForm {

    private List<MultipartFile> files;
    private Long parentId;

    public FileUploadForm() {
    }

    public FileUploadForm(Long parentId) {
        this.parentId = parentId;
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

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }
}