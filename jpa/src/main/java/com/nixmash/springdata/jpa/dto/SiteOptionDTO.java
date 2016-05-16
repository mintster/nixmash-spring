package com.nixmash.springdata.jpa.dto;

import com.nixmash.springdata.jpa.model.SiteOption;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class SiteOptionDTO {

    public SiteOptionDTO() {
    }

    private Long optionId;

    @NotEmpty
    @Length(max = SiteOption.MAX_LENGTH_PROPERTYNAME)
    private String name;

    private String value;

    public SiteOptionDTO(String name, String value) {
        this.name = name;
        this.value = value;
    }

    // region getter setters

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

    // endregion

    // region toString()

    @Override
    public String toString() {
        return "SiteOptionDTO{" +
                "optionId=" + optionId +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    // endregion

    public static Builder with(String name, Object value) {
        return new SiteOptionDTO.Builder(name, value);
    }

    public static class Builder {

        private SiteOptionDTO built;

        public Builder(String name, Object value) {
            built = new SiteOptionDTO();
            built.setName(name);
            if (value != null)
                built.setValue(String.valueOf(value));
        }

        public SiteOptionDTO build() {
            return built;
        }
    }
}
