package com.nixmash.springdata.jpa.dto;

import com.nixmash.springdata.jpa.enums.UserRegistration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class SiteOptionMapDTO implements Serializable{
    private static final long serialVersionUID = -5465065342755162883L;

    public SiteOptionMapDTO() {
    }

    @NotEmpty
    private String siteName;

    @NotEmpty
    private String siteDescription;

    @NotNull
    private Boolean addGoogleAnalytics;

    private String googleAnalyticsTrackingId;

    @NotNull
    private Integer integerProperty;

    private UserRegistration userRegistration;

    // region getters setters

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

    public Integer getIntegerProperty() {
        return integerProperty;
    }

    public void setIntegerProperty(Integer integerProperty) {
        this.integerProperty = integerProperty;
    }

    public UserRegistration getUserRegistration() {
        return userRegistration;
    }

    public void setUserRegistration(UserRegistration userRegistration) {
        this.userRegistration = userRegistration;
    }

    // endregion

    public static Builder withGeneralSettings(
            String siteName,
            String siteDescription,
            Boolean addGoogleAnalytics,
            String googleAnalyticsTrackingId,
            UserRegistration userRegistration){

        return new Builder(siteName, siteDescription, addGoogleAnalytics, googleAnalyticsTrackingId, userRegistration);
    }

    public static class Builder {

        private SiteOptionMapDTO built;

        public Builder(String siteName, String siteDescription, Boolean addGoogleAnalytics, String googleAnalyticsTrackingId, UserRegistration userRegistration) {
            built = new SiteOptionMapDTO();
            built.siteName = siteName;
            built.siteDescription = siteDescription;
            built.addGoogleAnalytics = addGoogleAnalytics;
            built.googleAnalyticsTrackingId = googleAnalyticsTrackingId;
            built.userRegistration = userRegistration;
        }

        public Builder integerProperty(Integer integerProperty) {
            built.integerProperty = integerProperty;
            return this;
        }

        public SiteOptionMapDTO build() {
            return built;
        }
    }
}
