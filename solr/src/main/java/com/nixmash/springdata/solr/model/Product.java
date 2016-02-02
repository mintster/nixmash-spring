/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nixmash.springdata.solr.model;

import java.util.List;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.geo.Point;
import org.springframework.data.solr.core.geo.GeoConverters;

/**
 * @author Christoph Strobl
 */
public class Product implements IProduct {

	@Field(ID_FIELD)
	private String id;

	@Field(NAME_FIELD)
	private String name;

	@Field(FEATURE_FIELD)
	private List<String> features;

	@Field(CATEGORY_FIELD)
	private List<String> categories;

	@Field(WEIGHT_FIELD)
	private Float weight;

	@Field(PRICE_FIELD)
	private Float price;

	@Field(POPULARITY_FIELD)
	private Integer popularity;

	@Field(AVAILABLE_FIELD)
	private boolean available;

	@Field(DOCTYPE_FIELD)
	private String doctype;

	@Field(LOCATION_FIELD)
	private String location;

	private Point point;

	public Product() {
	};

	public Product(String name) {
		setName(name);
	}

	public Product(String id, String name) {
		setId(id);
		setName(name);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<String> getFeatures() {
		return features;
	}

	public void setFeatures(List<String> features) {
		this.features = features;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Integer getPopularity() {
		return popularity;
	}

	public void setPopularity(Integer popularity) {
		this.popularity = popularity;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getDoctype() {
		return doctype;
	}

	public void setDoctype(String doctype) {
		this.doctype = doctype;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public Point getPoint() {
		String _location = this.getLocation();
		if (this.getLocation() == null)
			_location = "-1,-1";
		return GeoConverters.StringToPointConverter.INSTANCE.convert(_location);
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	   public boolean hasCategories() {
	        return (this.categories != null);
	    }
	   
	   public boolean hasFeatures() {
	        return (this.features != null);
	    }
	   
	   public boolean hasLocation() {
		   return (this.getLocation() != null);
	   }

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", categories=" + categories + ", features=" + features + ", weight=" + weight + ", price="
				+ price + ", popularity=" + popularity + ", available=" + available + ", doctype=" + doctype + "]";
	}

}
