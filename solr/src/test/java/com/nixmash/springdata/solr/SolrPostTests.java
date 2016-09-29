package com.nixmash.springdata.solr;

import com.nixmash.springdata.solr.model.PostDoc;
import com.nixmash.springdata.solr.repository.custom.CustomPostRepository;
import com.nixmash.springdata.solr.service.PostService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
public class SolrPostTests extends SolrContext {

	private static final int INITIAL_POST_COUNT = 7;
	private static final int BOOTSTRAP_POST_COUNT = 2;

	@Autowired
	SolrOperations solrOperations;

	@After
	public void tearDown() {
		Query query = new SimpleQuery(new SimpleStringCriteria("cat:test*"));
		solrOperations.delete(query);
		solrOperations.commit();
	}

	@Resource
	CustomPostRepository repo;

	@Autowired
	private PostService postService;

	@Test
	public void retrieveAllPostCount() {
		Query query = new SimpleQuery(new SimpleStringCriteria("doctype:post"));
		Page<PostDoc> posts = solrOperations.queryForPage(query, PostDoc.class);
		Assert.assertEquals(INITIAL_POST_COUNT, posts.getTotalElements());
	}

	@Test
	public void getPostsWithUserQuery() {
		List<PostDoc> posts = postService.getPostsWithUserQuery("bootstrap");
		Assert.assertEquals(BOOTSTRAP_POST_COUNT, posts.size());
	}

	private Sort sortByIdDesc() {
		return new Sort(Sort.Direction.DESC, PostDoc.ID);
	}

}
