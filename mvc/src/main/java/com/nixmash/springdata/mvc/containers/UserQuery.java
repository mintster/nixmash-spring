package com.nixmash.springdata.mvc.containers;

public class UserQuery implements java.io.Serializable {

	private static final long serialVersionUID = -3747394871627673122L;

	public UserQuery() {}

	private  String query;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
}

