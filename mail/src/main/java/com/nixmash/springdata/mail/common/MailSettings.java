package com.nixmash.springdata.mail.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("file:/home/daveburke/web/nixmashspring/mail.properties")
@ConfigurationProperties(prefix = "mail")
public class MailSettings {

	private String serverHost;
	private Integer serverPort;
	private String serverUsername;
	private String serverPassword;

	private Boolean smtpAuth;
	private Boolean smtpStartTlsEnable;

	private String contactTo;

	public String getContactTo() {
		return contactTo;
	}
	public void setContactTo(String contactTo) {
		this.contactTo = contactTo;
	}

	public String getServerUsername() {
		return serverUsername;
	}
	public void setServerUsername(String serverUsername) {
		this.serverUsername = serverUsername;
	}

	public String getServerPassword() {
		return serverPassword;
	}
	public void setServerPassword(String serverPassword) {
		this.serverPassword = serverPassword;
	}

	public Boolean getSmtpAuth() {
		return smtpAuth;
	}
	public void setSmtpAuth(Boolean smtpAuth) {
		this.smtpAuth = smtpAuth;
	}

	public Boolean getSmtpStartTlsEnable() {
		return smtpStartTlsEnable;
	}
	public void setSmtpStartTlsEnable(Boolean smtpStartTlsEnable) {
		this.smtpStartTlsEnable = smtpStartTlsEnable;
	}

	public Integer getServerPort() {
		return serverPort;
	}
	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}

	public String getServerHost() {
		return serverHost;
	}
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}
}
