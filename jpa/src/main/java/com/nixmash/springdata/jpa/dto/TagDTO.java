package com.nixmash.springdata.jpa.dto;

import com.nixmash.springdata.jpa.model.Tag;

import java.io.Serializable;

/**
 * Created by daveburke on 6/22/16.
 */
public class TagDTO implements Serializable {

    private static final long serialVersionUID = -4809849404139121173L;

    private long tagId = -1;
    private String tagValue;
    private int tagCount = 0;

    public TagDTO() {
    }

    public TagDTO(String tagValue) {
        this.tagValue = tagValue;
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

    public int getTagCount() {
        return tagCount;
    }

    public void setTagCount(int tagCount) {
        this.tagCount = tagCount;
    }

    public TagDTO(long tagId, String tagValue) {
        this.tagId = tagId;
        this.tagValue = tagValue;
    }

    public TagDTO(Tag tag) {
        this.tagId = tag.getTagId();
        this.tagValue = tag.getTagValue();
        this.tagCount = tag.getPosts().size();
    }
}

