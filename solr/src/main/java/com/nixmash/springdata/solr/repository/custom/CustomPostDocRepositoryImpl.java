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

import com.nixmash.springdata.jpa.dto.PostQueryDTO;
import com.nixmash.springdata.jpa.enums.PostType;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.utils.PostUtils;
import com.nixmash.springdata.solr.enums.SolrDocType;
import com.nixmash.springdata.solr.model.IPostDoc;
import com.nixmash.springdata.solr.model.PostDoc;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.UncategorizedSolrException;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.PartialUpdate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class CustomPostDocRepositoryImpl implements CustomBasePostDocRepository {

	private static final Logger logger = LoggerFactory.getLogger(CustomBasePostDocRepository.class);

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

	@Transactional(rollbackFor = {UncategorizedSolrException.class})
	@Override
	public void update(Post post) {
		logger.debug("Performing partial update for post: {}", post);


		PartialUpdate update = new PartialUpdate(PostDoc.ID, post.getPostId().toString());

		update.add(PostDoc.POST_TITLE, post.getPostTitle());
		update.add(PostDoc.HTML, post.getPostContent());
		update.add(PostDoc.POST_TEXT, Jsoup.parse(post.getPostContent()).text());
		update.add(PostDoc.TAG, PostUtils.tagsToTagValues(post.getTags()));

		solrTemplate.saveBean(update);
		solrTemplate.commit();
	}

	@Override
	public Page<PostDoc> pagedQuickSearch(String searchTerms, PageRequest pageRequest) {
		logger.info("Paged Title and Body Quick Search with : {}", searchTerms);
		Query query = new SimpleQuery(titleAndBodyCriteria(searchTerms));
		query.setRows(1000);
		query.setPageRequest(pageRequest);
		Page<PostDoc> results = solrTemplate.queryForPage(query, PostDoc.class);
		return results;
	}

	@Override
	public Page<PostDoc> pagedFullSearch(PostQueryDTO postQueryDTO, PageRequest pageRequest) {
		Query query = new SimpleQuery(postQueryDTO.getQuery());

		if (!postQueryDTO.getPostType().equals(PostType.UNDEFINED)) {
			query.addFilterQuery(new SimpleQuery(new Criteria(IPostDoc.POST_TYPE).is(postQueryDTO.getPostType())));
		}

		query.addFilterQuery(new SimpleQuery(new Criteria(IPostDoc.DOCTYPE).is(SolrDocType.POST)));
		query.setRows(1000);
		query.setPageRequest(pageRequest);

		Page<PostDoc> results = solrTemplate.queryForPage(query, PostDoc.class);
		return results;
	}

	@Override
	public List<PostDoc> fullSearch(PostQueryDTO postQueryDTO) {
		Query query = new SimpleQuery(postQueryDTO.getQuery());

		if (!postQueryDTO.getPostType().equals(PostType.UNDEFINED)) {
			query.addFilterQuery(new SimpleQuery(new Criteria(IPostDoc.POST_TYPE).is(postQueryDTO.getPostType())));
		}

		query.addFilterQuery(new SimpleQuery(new Criteria(IPostDoc.DOCTYPE).is(SolrDocType.POST)));
		query.setRows(1000);

		Page<PostDoc> results = solrTemplate.queryForPage(query, PostDoc.class);
		return results.getContent();
	}

	@Override
	public List<PostDoc> quickSearch(String searchTerms) {
		logger.info("Searching Title and Body with Multiple Terms : {}", searchTerms);
		Query query = new SimpleQuery(titleAndBodyCriteria(searchTerms));
		query.setRows(1000);
		Page<PostDoc> results = solrTemplate.queryForPage(query, PostDoc.class);
		return results.getContent();
	}

	private Criteria titleAndBodyCriteria(String searchTerms) {
		String[] words = searchTerms.split(" ");
		Criteria criteria = new Criteria();
		for (String word : words) {
			criteria = criteria.and(new Criteria(PostDoc.POST_TITLE).contains(word)
					.or(PostDoc.POST_TEXT).contains(word));
		}
		return criteria.and(new Criteria(PostDoc.DOCTYPE).is("post"));
	}

}
