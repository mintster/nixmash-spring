package com.nixmash.springdata.solr.repository.factory;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.PartialUpdate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

import com.nixmash.springdata.solr.model.IProduct;
import com.nixmash.springdata.solr.model.Product;

/**
 * 
 * NixMash Spring Notes: ---------------------------------------------------
 * 
 * Based on Petri Kainulainen's Spring Data Solr Tutorial at
 * http://www.petrikainulainen.net/spring-data-solr-tutorial/
 *
 */
public class CustomBaseRepositoryImpl<T, ID extends Serializable> extends SimpleSolrRepository<T, ID>
		implements CustomBaseRepository<T, ID> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomBaseRepositoryImpl.class);

	public CustomBaseRepositoryImpl(SolrOperations solrOperations, Class<T> entityClass) {
		super(solrOperations, entityClass);
	}

	@Override
	public long count(String searchTerm) {
		LOGGER.debug("Finding count for search term: {}", searchTerm);

		String[] words = searchTerm.split(" ");
		Criteria conditions = createSearchConditions(words);
		SimpleQuery countQuery = new SimpleQuery(conditions);

		return getSolrOperations().count(countQuery);
	}

	private Criteria createSearchConditions(String[] words) {
		Criteria conditions = null;

		for (String word : words) {
			if (conditions == null) {
				conditions = new Criteria(IProduct.NAME_FIELD).contains(word);
			} else {
				conditions = conditions.or(new Criteria(IProduct.CATEGORY_FIELD).contains(word))
						.or(new Criteria(IProduct.NAME_FIELD).contains(word));
			}
		}

		return conditions;
	}

	@Override
	public void update(Product product) {
		LOGGER.debug("Performing partial update for todo entry: {}", product);

		PartialUpdate update = new PartialUpdate(IProduct.ID_FIELD, product.getId().toString());

		update.add(IProduct.NAME_FIELD, product.getName());

		getSolrOperations().saveBean(update);
		getSolrOperations().commit();
	}
}
