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
package com.nixmash.springdata.solr.repository.custom;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import com.nixmash.springdata.solr.model.IProduct;
import com.nixmash.springdata.solr.model.Product;

/**
 * 
 * NixMash Spring Notes: ---------------------------------------------------
 * 
 * Based on Christoph Strobl's Spring Solr Repository Example for Spring Boot
 * 
 * On GitHub: https://goo.gl/JoAYaT
 * 
 */
public interface CustomProductRepository extends CustomBaseRepository, SolrCrudRepository<Product, String> {

	Page<Product> findByPopularity(Integer popularity, Pageable page);

	List<Product> findByNameStartingWith(String name);

	Page<Product> findByAvailableTrue(Pageable page);

	@Query(IProduct.AVAILABLE_FIELD + ":false")
	Page<Product> findByAvailableFalseUsingAnnotatedQuery(Pageable page);

	public List<Product> findByNameContainsOrCategoriesContains(String title, String category, Sort sort);

	@Query(name = "Product.findByNamedQuery")
	public List<Product> findByNamedQuery(String searchTerm, Sort sort);

	@Query("name:*?0* OR cat:*?0*")
	public List<Product> findByQueryAnnotation(String searchTerm, Sort sort);

}
