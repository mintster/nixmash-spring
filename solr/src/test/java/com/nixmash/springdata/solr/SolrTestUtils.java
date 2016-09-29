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
package com.nixmash.springdata.solr;

import com.nixmash.springdata.solr.enums.SolrDocType;
import com.nixmash.springdata.solr.model.PostDoc;
import com.nixmash.springdata.solr.model.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 
 * NixMash Spring Notes: ---------------------------------------------------
 * 
 * Based on Christoph Strobl's Spring Solr Repository Example for Spring Boot
 * 
 * On GitHub: https://goo.gl/JoAYaT
 * 
 */
public abstract class SolrTestUtils {

	private static List<String> categories = Collections.singletonList("test");
	private static List<String> tags = Arrays.asList("testone", "testtwo");


	protected static List<Product> createProductList(int nrProducts) {
		List<Product> products = new ArrayList<Product>(nrProducts);
		for (int i = 0; i < nrProducts; i++) {
			products.add(createProduct(i));
		}
		return products;
	}

	protected static Product createProduct(int id) {
		Product product = new Product();
		product.setId(Integer.toString(id));
		product.setAvailable(id % 2 == 0);
		product.setName("product-" + id);
		product.setPopularity(id * 10);
		product.setCategories(categories);
		product.setPrice((float) id * 100);
		product.setWeight((float) id * 2);
		product.setLocation(null);
		product.setDoctype(SolrDocType.PRODUCT);
		return product;
	}

	protected static List<PostDoc> createPostList(int nrPosts) {
		List<PostDoc> posts = new ArrayList<PostDoc>(nrPosts);
		for (int i = 0; i < nrPosts; i++) {
			posts.add(createPost(i));
		}
		return posts;
	}

	protected static PostDoc createPost(int id) {
		PostDoc post = new PostDoc();
		post.setPostId(Integer.toString(id));
		post.setPostTitle("Post Title " + id);
		post.setPostAuthor("mintster");
		post.setPostName("post-title-" + id);
		post.setPostLink(id % 2 == 0 ? "" : "http://somewhere/" + id);
		post.setPostType(id % 2 == 0 ? "POST" : "LINK");
		post.setPostContent("<strong>Post Content</strong> " + id);
		post.setPostText("Post Content " + id);
		post.setPostSource(id % 2 == 0 ? "somewhere.com" : "NA");
		post.setTags(tags);
		post.setDocType(SolrDocType.POST);
		return post;
	}
}
