package com.nixmash.springdata.solr.enums;

import org.springframework.data.solr.core.query.Field;

import com.nixmash.springdata.solr.model.IProduct;

public enum SolrProductField implements Field {

	// @formatter:off
	
	ID(IProduct.ID_FIELD), 
	NAME(IProduct.NAME_FIELD), 
	PRICE(IProduct.PRICE_FIELD), 
	AVAILABLE(IProduct.AVAILABLE_FIELD), 
	CATEGORY(IProduct.CATEGORY_FIELD), 
	WEIGHT(IProduct.WEIGHT_FIELD), 
	POPULARITY(IProduct.POPULARITY_FIELD),
	DOCTYPE(IProduct.DOCTYPE_FIELD);

	// @formatter:on

	private final String fieldName;

	private SolrProductField(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public String getName() {
		return fieldName;
	}

}
