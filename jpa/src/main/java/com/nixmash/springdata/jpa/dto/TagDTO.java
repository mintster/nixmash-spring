package com.nixmash.springdata.jpa.dto;

import java.io.Serializable;

/**
 * Created by daveburke on 6/22/16.
 */
public class TagDTO implements Serializable {

    private static final long serialVersionUID = -4809849404139121173L;

    private Integer tagId = -1;
    private String tagValue;

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public TagDTO(Integer tagId, String tagValue) {
        this.tagId = tagId;
        this.tagValue = tagValue;
    }

}
