package com.nixmash.springdata.jpa.common;

import org.springframework.stereotype.Component;

/**
 * Created by daveburke on 5/8/16.
 */
@Component
public class SiteOptions {

    public String siteName;
    public String siteDescription;

    public Boolean addGoogleAnalytics;
    public String googleAnalyticsTrackingId;

    public Integer integerProperty;

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
}
