package com.nixmash.springdata.solr.repository;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Repository;

import com.nixmash.springdata.solr.model.Product;

@Repository
public class ProductRepositoryImpl implements ProductSolrRepository {

	private static final Logger logger = LoggerFactory.getLogger(ProductRepositoryImpl.class);

	@Resource
	private SolrTemplate solrTemplate;

	@Override
	public List<Product> search(String searchTerm) {
		logger.debug("Building a criteria query with search term: {}", searchTerm);

		String[] words = searchTerm.split(" ");

		Criteria conditions = createSearchConditions(words);
		SimpleQuery search = new SimpleQuery(conditions);
		search.addSort(sortByIdDesc());

		Page<Product> results = solrTemplate.queryForPage(search, Product.class);
		return results.getContent();
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

	private Sort sortByIdDesc() {
		return new Sort(Sort.Direction.DESC, Product.ID_FIELD);
	}

}
