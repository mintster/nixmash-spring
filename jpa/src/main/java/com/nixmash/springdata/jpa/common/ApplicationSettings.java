package com.nixmash.springdata.jpa.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("file:/home/daveburke/web/nixmashspring/external.properties")
@ConfigurationProperties(prefix = "external")
public class ApplicationSettings {

	private Boolean isDemoSite;

	private String twitterAppId;
	private String twitterAppSecret;
	private String facebookAppId;
	private String facebookAppSecret;
	private String googleAppId;
	private String googleAppSecret;

	public String getGoogleAppId() {
		return googleAppId;
	}

	public void setGoogleAppId(String googleAppId) {
		this.googleAppId = googleAppId;
	}

	public String getGoogleAppSecret() {
		return googleAppSecret;
	}

	public void setGoogleAppSecret(String googleAppSecret) {
		this.googleAppSecret = googleAppSecret;
	}

	private  String siteName;

	public String getTwitterAppId() {
		return twitterAppId;
	}

	public void setTwitterAppId(String twitterAppId) {
		this.twitterAppId = twitterAppId;
	}

	public String getTwitterAppSecret() {
		return twitterAppSecret;
	}

	public void setTwitterAppSecret(String twitterAppSecret) {
		this.twitterAppSecret = twitterAppSecret;
	}

	public String getFacebookAppId() {
		return facebookAppId;
	}

	public void setFacebookAppId(String facebookAppId) {
		this.facebookAppId = facebookAppId;
	}

	public String getFacebookAppSecret() {
		return facebookAppSecret;
	}

	public void setFacebookAppSecret(String facebookAppSecret) {
		this.facebookAppSecret = facebookAppSecret;
	}

	public Boolean getIsDemoSite() {
		return isDemoSite;
	}

	public void setIsDemoSite(Boolean isDemoSite) {
		this.isDemoSite = isDemoSite;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

}
