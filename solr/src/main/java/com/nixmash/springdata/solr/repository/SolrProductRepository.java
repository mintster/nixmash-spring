package com.nixmash.springdata.solr.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleField;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

import com.nixmash.springdata.solr.enums.SolrDocType;
import com.nixmash.springdata.solr.model.IProduct;
import com.nixmash.springdata.solr.model.Product;

@NoRepositoryBean
public class SolrProductRepository extends SimpleSolrRepository<Product, String>implements ProductRepository {

	@Override
	public List<Product> findByAvailableTrue() {
		Query query = new SimpleQuery(new Criteria(new SimpleField(Criteria.WILDCARD)).expression(Criteria.WILDCARD));
		query.addFilterQuery(new SimpleQuery(new Criteria(IProduct.DOCTYPE_FIELD).is(SolrDocType.PRODUCT)));
		query.setRows(1000);
		Page<Product> results = getSolrOperations().queryForPage(query, Product.class);
		return results.getContent();
	}

}
