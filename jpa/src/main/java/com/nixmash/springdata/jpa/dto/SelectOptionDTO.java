package com.nixmash.springdata.jpa.dto;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: daveburke
 * Date: 10/1/15
 * Time: 10:44 AM
 */
public class SelectOptionDTO implements Serializable {


    private static final long serialVersionUID = -8003039691215392613L;

    private String label;
    private String value;
    private Boolean selected;

    public SelectOptionDTO(String label, String value, Boolean selected) {
        this.label = label;
        this.value = value;
        this.selected = selected;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }


}
