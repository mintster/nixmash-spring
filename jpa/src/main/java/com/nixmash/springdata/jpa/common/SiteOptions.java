package com.nixmash.springdata.jpa.common;

import com.nixmash.springdata.jpa.model.SiteOption;
import com.nixmash.springdata.jpa.repository.SiteOptionRepository;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

@Component
@DependsOn("siteOptionRepository")
public class SiteOptions {

    private static final Logger logger = LoggerFactory.getLogger(SiteOptions.class);


    private String siteName;
    private String siteDescription;

    private Boolean addGoogleAnalytics;
    private String googleAnalyticsTrackingId;

    private Integer integerProperty;

    SiteOptionRepository siteOptionRepository;

    @Autowired
    public SiteOptions(SiteOptionRepository siteOptionRepository) {
        this.siteOptionRepository = siteOptionRepository;
    }

    // region Getter Setters

    public Integer getIntegerProperty() {
        return integerProperty;
    }
    public void setIntegerProperty(Integer integerProperty) {
        this.integerProperty = integerProperty;
    }

    public String getSiteName() {
        return siteName;
    }
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteDescription() {
        return siteDescription;
    }
    public void setSiteDescription(String siteDescription) {
        this.siteDescription = siteDescription;
    }

    public Boolean getAddGoogleAnalytics() {
        return addGoogleAnalytics;
    }
    public void setAddGoogleAnalytics(Boolean addGoogleAnalytics) {
        this.addGoogleAnalytics = addGoogleAnalytics;
    }

    public String getGoogleAnalyticsTrackingId() {
        return googleAnalyticsTrackingId;
    }
    public void setGoogleAnalyticsTrackingId(String googleAnalyticsTrackingId) {
        this.googleAnalyticsTrackingId = googleAnalyticsTrackingId;
    }

    // endregion

    // region Constants

    public static final String OPTION_VALUE_TYPE_BOOLEAN= "Boolean";
    public static final String OPTION_VALUE_TYPE_INTEGER= "Integer";

    // endregion

    // region ToString

    @Override
    public String toString() {
        return "SiteOptions{" +
                "siteName='" + siteName + '\'' +
                ", siteDescription='" + siteDescription + '\'' +
                ", addGoogleAnalytics=" + addGoogleAnalytics +
                ", googleAnalyticsTrackingId='" + googleAnalyticsTrackingId + '\'' +
                ", integerProperty=" + integerProperty +
                '}';
    }


    // endregion

    public void setSiteOptionProperty(String property, Object value)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (PropertyUtils.isWriteable(this, property)) {

            switch (PropertyUtils
                    .getPropertyDescriptor(this, property).getPropertyType().getSimpleName()) {
                case OPTION_VALUE_TYPE_BOOLEAN:
                    value = Boolean.valueOf(value.toString());
                    break;
                case OPTION_VALUE_TYPE_INTEGER:
                    value = Integer.parseInt(value.toString());
                    break;
                default:
                    break;
            }
            PropertyUtils.setProperty(this, property, value);
        }
    }

    @PostConstruct
    public void init() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Collection<SiteOption> siteOptionKeyValues = siteOptionRepository.findAll();
        Map<String, Object> options = new Hashtable<>();
        for (SiteOption siteOption : siteOptionKeyValues) {
            options.put(siteOption.getName(), siteOption.getValue());
        }

        for (String key : options.keySet()) {
            for (Field f : this.getClass().getDeclaredFields()) {
                if (f.getName().toUpperCase().equals(key.toUpperCase())) {
                    setSiteOptionProperty(key, options.get(key));
                }
            }
        }
    }
}
