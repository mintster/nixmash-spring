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

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.PartialUpdate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.stereotype.Repository;

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
@Repository
public class CustomProductRepositoryImpl implements CustomBaseRepository {

	private static final Logger logger = LoggerFactory.getLogger(CustomProductRepositoryImpl.class);

	@Resource
	private SolrTemplate solrTemplate;

	@Override
	public Page<Product> findTestCategoryRecords() {
		return solrTemplate.queryForPage(
				new SimpleQuery(new SimpleStringCriteria("cat:test")).setPageRequest(new PageRequest(0, 100)),
				Product.class);
	}

	@Override
	public Page<Product> findProductsByCustomImplementation(String value, Pageable page) {
		return solrTemplate.queryForPage(
				new SimpleQuery(new SimpleStringCriteria("name:" + value)).setPageRequest(page), Product.class);
	}

	@Override
	public void updateProductCategory(String productId, List<String> categories) {
		PartialUpdate update = new PartialUpdate(IProduct.ID_FIELD, productId);
		update.setValueOfField(IProduct.CATEGORY_FIELD, categories);
		solrTemplate.saveBean(update);
		solrTemplate.commit();
	}

	@Override
	public void update(Product product) {
		logger.debug("Performing partial update for todo entry: {}", product);
		PartialUpdate update = new PartialUpdate(Product.ID_FIELD, product.getId().toString());
		update.add(Product.NAME_FIELD, product.getName());
		solrTemplate.saveBean(update);
		solrTemplate.commit();
	}
}
