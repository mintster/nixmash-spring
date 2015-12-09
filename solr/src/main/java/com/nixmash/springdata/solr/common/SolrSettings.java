package com.nixmash.springdata.solr.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("file:/home/daveburke/web/nixmashspring/solr.properties")
@ConfigurationProperties(prefix="solr")
public class SolrSettings {

	private String solrServerUrl;
	private String solrEmbeddedPath;

	public String getSolrServerUrl() {
		return solrServerUrl;
	}

	public void setSolrServerUrl(String solrServerUrl) {
		this.solrServerUrl = solrServerUrl;
	}

	public String getSolrEmbeddedPath() {
		return solrEmbeddedPath;
	}

	public void setSolrEmbeddedPath(String solrEmbeddedPath) {
		this.solrEmbeddedPath = solrEmbeddedPath;
	}
	
}
