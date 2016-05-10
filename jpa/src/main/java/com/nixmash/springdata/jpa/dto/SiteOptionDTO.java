package com.nixmash.springdata.jpa.dto;

import com.nixmash.springdata.jpa.model.SiteOption;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class SiteOptionDTO {

    private Long optionId;

    @NotEmpty
    @Length(max = SiteOption.MAX_LENGTH_PROPERTYNAME)
    private String name;

    @NotEmpty
    private String value;

    public SiteOptionDTO(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
