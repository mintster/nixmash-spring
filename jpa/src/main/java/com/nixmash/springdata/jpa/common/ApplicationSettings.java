package com.nixmash.springdata.jpa.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("file:/home/daveburke/web/nixmashspring/external.properties")
@ConfigurationProperties(prefix = "external")
public class ApplicationSettings {

	private Boolean isDemoSite;
	private String baseUrl;
	private String siteName;

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

	private String rssChannelTitle;
	private String rssChannelDescription;

	//endregion

	// region post Uploads

	private String postImagePath;
	private String postDemoImagePath;
	private String postImageUrlRoot;
	private String postDemoImageUrlRoot;

	private String flashcardImagePath;
	private String flashcardImageUrlRoot;

	// endregion

	//region Addons

	private int defaultFlashcardCategory;

	//endregion


	// region Posts

	private int sidebarTagCloudCount;

	// endregion

	public int getSidebarTagCloudCount() {
		return sidebarTagCloudCount;
	}

	public void setSidebarTagCloudCount(int sidebarTagCloudCount) {
		this.sidebarTagCloudCount = sidebarTagCloudCount;
	}

	public int getDefaultFlashcardCategory() {
		return defaultFlashcardCategory;
	}

	public void setDefaultFlashcardCategory(int defaultFlashcardCategory) {
		this.defaultFlashcardCategory = defaultFlashcardCategory;
	}

	public String getFlashcardImagePath() {
		return flashcardImagePath;
	}

	public void setFlashcardImagePath(String flashcardImagePath) {
		this.flashcardImagePath = flashcardImagePath;
	}

	public String getFlashcardImageUrlRoot() {
		return flashcardImageUrlRoot;
	}

	public void setFlashcardImageUrlRoot(String flashcardImageUrlRoot) {
		this.flashcardImageUrlRoot = flashcardImageUrlRoot;
	}

	public String getPostDemoImagePath() {
		return postDemoImagePath;
	}

	public void setPostDemoImagePath(String postDemoImagePath) {
		this.postDemoImagePath = postDemoImagePath;
	}

	public String getPostDemoImageUrlRoot() {
		return postDemoImageUrlRoot;
	}

	public void setPostDemoImageUrlRoot(String postDemoImageUrlRoot) {
		this.postDemoImageUrlRoot = postDemoImageUrlRoot;
	}

	public String getPostImagePath() {
		return postImagePath;
	}

	public void setPostImagePath(String postImagePath) {
		this.postImagePath = postImagePath;
	}

	public String getPostImageUrlRoot() {
		return postImageUrlRoot;
	}

	public void setPostImageUrlRoot(String postImageUrlRoot) {
		this.postImageUrlRoot = postImageUrlRoot;
	}

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

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
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

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
}
