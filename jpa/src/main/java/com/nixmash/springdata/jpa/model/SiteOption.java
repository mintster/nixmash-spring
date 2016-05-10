package com.nixmash.springdata.jpa.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "site_options")
public class SiteOption implements Serializable {

    private static final long serialVersionUID = 6690621866489266673L;
    public static final int MAX_LENGTH_PROPERTYNAME = 50;

    @Id
    @Column(name = "option_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;

    @Column(name = "option_name")
    @NotEmpty
    private String name;

    @Column(name = "option_value", columnDefinition="TEXT")
    private String value;

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

    public void update(final String optionName, final String optionValue) {
        this.name = optionName;
        this.value= optionValue;
    }
}
