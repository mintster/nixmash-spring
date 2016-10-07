package com.nixmash.springdata.jpa.dto;

import com.nixmash.springdata.jpa.enums.PostType;
import org.hibernate.validator.constraints.NotEmpty;

public class PostQueryDTO implements java.io.Serializable {

	private static final long serialVersionUID = -8106151946403787355L;

	public PostQueryDTO() {}

	@NotEmpty
	private  String query;

	private PostType postType;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public PostType getPostType() {
		return postType;
	}

	public void setPostType(PostType postType) {
		this.postType = postType;
	}

	public PostQueryDTO(String query, PostType postType) {
		this.query = query;
		this.postType = postType;
	}

	public PostQueryDTO(String query) {
		this.query = query;
		this.postType = PostType.UNDEFINED;
	}
}

