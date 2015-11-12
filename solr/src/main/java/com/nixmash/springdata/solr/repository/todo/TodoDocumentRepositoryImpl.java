package com.nixmash.springdata.solr.repository.todo;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.PartialUpdate;
import org.springframework.stereotype.Repository;

import com.nixmash.springdata.solr.model.Product;

/**
 * 
 * NixMash Spring Notes: ---------------------------------------------------
 * 
 * Based on Petri Kainulainen's Spring Data Solr Tutorial at
 * http://www.petrikainulainen.net/spring-data-solr-tutorial/
 *
 */
@Repository
public class TodoDocumentRepositoryImpl implements PartialUpdateRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(TodoDocumentRepositoryImpl.class);

	@Resource
	private SolrTemplate solrTemplate;

	@Override
	public void update(Product product) {
		LOGGER.debug("Performing partial update for todo entry: {}", product);
		PartialUpdate update = new PartialUpdate(Product.ID_FIELD, product.getId().toString());
		update.add(Product.NAME_FIELD, product.getName());
		solrTemplate.saveBean(update);
		solrTemplate.commit();
	}
}
