package com.nixmash.springdata.jpa.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("file:/home/daveburke/web/nixmashspring/external.properties")
@ConfigurationProperties(prefix = "external")
public class ApplicationSettings {

	private Boolean isDemoSite;

	//region Social Properties

	private String twitterAppId;
	private String twitterAppSecret;
	private String facebookAppId;
	private String facebookAppSecret;
	private String googleAppId;
	private String googleAppSecret;
	private String googleMapKey;

	//endregion

	// region User Profile Image Properties

	private String profileImagePath;
	private String profileImageUrlRoot;

	private String profileIconPath;
	private String profileIconUrlRoot;

	private String editorImagePlacemarker;

	// endregion

	//region RSS Properties

	private String rssBaseUrl;
	private String rssChannelTitle;
	private String rssChannelDescription;

	//endregion

	public String getEditorImagePlacemarker() {
		return editorImagePlacemarker;
	}

	public void setEditorImagePlacemarker(String editorImagePlacemarker) {
		this.editorImagePlacemarker = editorImagePlacemarker;
	}

	public String getGoogleMapKey() {
		return googleMapKey;
	}

	public void setGoogleMapKey(String googleMapKey) {
		this.googleMapKey = googleMapKey;
	}

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

	public String getProfileImagePath() {
		return profileImagePath;
	}

	public void setProfileImagePath(String profileImagePath) {
		this.profileImagePath = profileImagePath;
	}

	public String getProfileImageUrlRoot() {
		return profileImageUrlRoot;
	}

	public void setProfileImageUrlRoot(String profileImageUrlRoot) {
		this.profileImageUrlRoot = profileImageUrlRoot;
	}

	public String getProfileIconUrlRoot() {
		return profileIconUrlRoot;
	}

	public void setProfileIconUrlRoot(String profileIconUrlRoot) {
		this.profileIconUrlRoot = profileIconUrlRoot;
	}

	public String getProfileIconPath() {
		return profileIconPath;
	}

	public void setProfileIconPath(String profileIconPath) {
		this.profileIconPath = profileIconPath;
	}

	public String getRssBaseUrl() {
		return rssBaseUrl;
	}

	public void setRssBaseUrl(String rssBaseUrl) {
		this.rssBaseUrl = rssBaseUrl;
	}

	public String getRssChannelTitle() {
		return rssChannelTitle;
	}

	public void setRssChannelTitle(String rssChannelTitle) {
		this.rssChannelTitle = rssChannelTitle;
	}

	public String getRssChannelDescription() {
		return rssChannelDescription;
	}

	public void setRssChannelDescription(String rssChannelDescription) {
		this.rssChannelDescription = rssChannelDescription;
	}
}
