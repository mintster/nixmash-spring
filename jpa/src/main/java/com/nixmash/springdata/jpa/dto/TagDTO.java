package com.nixmash.springdata.jpa.dto;

import java.io.Serializable;

/**
 * Created by daveburke on 6/22/16.
 */
public class TagDTO implements Serializable {

    private static final long serialVersionUID = -4809849404139121173L;

    private long tagId = -1;
    private String tagValue;

    public TagDTO() {
    }

    public TagDTO(String tagValue) {
        this.tagValue= tagValue;
    }


    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public TagDTO(long tagId, String tagValue) {
        this.tagId = tagId;
        this.tagValue = tagValue;
    }

}

