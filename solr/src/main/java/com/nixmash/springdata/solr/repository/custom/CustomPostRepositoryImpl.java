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

import com.nixmash.springdata.solr.enums.SolrDocType;
import com.nixmash.springdata.solr.model.IPostDoc;
import com.nixmash.springdata.solr.model.PostDoc;
import com.nixmash.springdata.solr.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class CustomPostRepositoryImpl implements CustomBasePostRepository {

	private static final Logger logger = LoggerFactory.getLogger(CustomBasePostRepository.class);

	@Resource
	private SolrTemplate solrTemplate;


	@Override
	public List<PostDoc> findPostsBySimpleQuery(String userQuery) {

		Query query = new SimpleQuery(userQuery);
		query.addFilterQuery(new SimpleQuery(new Criteria(IPostDoc.DOCTYPE).is(SolrDocType.POST)));
		query.setRows(1000);

		Page<PostDoc> results = solrTemplate.queryForPage(query, PostDoc.class);
		return results.getContent();
	}

	public Sort sortByIdDesc() {
		return new Sort(Sort.Direction.DESC, Product.ID_FIELD);
	}
}
