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
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.PartialUpdate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.stereotype.Repository;

import com.nixmash.springdata.solr.enums.SolrDocType;
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
	public List<Product> findProductsBySimpleQuery(String userQuery) {

		Query query = new SimpleQuery(userQuery);
		query.addFilterQuery(new SimpleQuery(new Criteria(IProduct.DOCTYPE_FIELD).is(SolrDocType.PRODUCT)));
		query.setRows(1000);

		Page<Product> results = solrTemplate.queryForPage(query, Product.class);
		return results.getContent();
	}

	@Override
	public void updateProductCategory(String productId, List<String> categories) {
		PartialUpdate update = new PartialUpdate(IProduct.ID_FIELD, productId);
		update.setValueOfField(IProduct.CATEGORY_FIELD, categories);
		solrTemplate.saveBean(update);
		solrTemplate.commit();
	}

	@Override
	public void updateProductName(Product product) {
		logger.debug("Performing partial update for todo entry: {}", product);
		PartialUpdate update = new PartialUpdate(Product.ID_FIELD, product.getId().toString());
		update.add(Product.NAME_FIELD, product.getName());
		solrTemplate.saveBean(update);
		solrTemplate.commit();
	}

	@Override
	public List<Product> searchWithCriteria(String searchTerm) {
		logger.debug("Building a criteria query with search term: {}", searchTerm);

		String[] words = searchTerm.split(" ");

		Criteria conditions = createSearchConditions(words);
		SimpleQuery search = new SimpleQuery(conditions);
		search.addSort(sortByIdDesc());

		Page<Product> results = solrTemplate.queryForPage(search, Product.class);
		return results.getContent();
	}

	@Override
	public HighlightPage<Product> searchProductsWithHighlights(String searchTerm) {
		SimpleHighlightQuery query = new SimpleHighlightQuery();
		String[] words = searchTerm.split(" ");
		Criteria conditions = createHighlightedNameConditions(words);
		query.addCriteria(conditions);

		HighlightOptions hlOptions = new HighlightOptions();
		hlOptions.addField("name");
		hlOptions.setSimplePrefix("<b>");
		hlOptions.setSimplePostfix("</b>");
		query.setHighlightOptions(hlOptions);

		return solrTemplate.queryForHighlightPage(query, Product.class);
	}

	private Criteria createHighlightedNameConditions(String[] words) {
		Criteria conditions = null;

		for (String word : words) {
			if (conditions == null) {
				conditions = new Criteria(Product.NAME_FIELD).contains(word);
			} else {
				conditions = conditions.or(new Criteria(Product.NAME_FIELD).contains(word));
			}
		}

		return conditions;
	}

	private Criteria createSearchConditions(String[] words) {
		Criteria conditions = null;

		for (String word : words) {
			if (conditions == null) {
				conditions = new Criteria(Product.NAME_FIELD).contains(word)
						.or(new Criteria(Product.CATEGORY_FIELD).contains(word));
			} else {
				conditions = conditions.or(new Criteria(Product.NAME_FIELD).contains(word))
						.or(new Criteria(Product.CATEGORY_FIELD).contains(word));
			}
		}

		return conditions;
	}

	public Sort sortByIdDesc() {
		return new Sort(Sort.Direction.DESC, Product.ID_FIELD);
	}
}
