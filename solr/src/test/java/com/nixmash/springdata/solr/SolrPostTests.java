package com.nixmash.springdata.solr;

import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.exceptions.PostNotFoundException;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.service.PostService;
import com.nixmash.springdata.jpa.utils.PostUtils;
import com.nixmash.springdata.solr.model.PostDoc;
import com.nixmash.springdata.solr.repository.custom.CustomPostDocRepository;
import com.nixmash.springdata.solr.service.PostDocService;
import com.nixmash.springdata.solr.utils.SolrUtils;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class SolrPostTests extends SolrContext {

	private static final int INITIAL_POST_COUNT = 7;
	private static final int BOOTSTRAP_POST_COUNT = 1;
	private List<Post> posts;
	private int postCount = 0;

	@Resource
	CustomPostDocRepository repo;

	@Autowired
	private PostDocService postDocService;

	@Autowired
	PostService postService;

	@Autowired
	SolrOperations solrOperations;

	@Test
	public void queryForPage() {
		Query query = new SimpleQuery(new SimpleStringCriteria("doctype:post"));
		Page<PostDoc> postDocs = solrOperations.queryForPage(query, PostDoc.class);
		assertEquals(postDocs.getTotalElements(), postCount);
	}

	@Test
	public void addPostWithRepository() throws Exception {
		// using postId 10 which is "Solr Rama"
		Post post = postService.getPostById(10L);
		PostDoc postDoc = SolrUtils.createPostDoc(post);
		repo.save(postDoc);

		PostDoc found = repo.findOne("10");
		assertEquals(found.getPostName(), "solr-rama");
		repo.delete("10");
	}

	@Test
	public void addPostWithAddToIndexService() throws Exception {
		// using postId 10 which is "Solr Rama"
		Post post = postService.getPostById(10L);
		postDocService.addToIndex(post);
		PostDoc found = repo.findOne("10");
		assertEquals(found.getPostName(), "solr-rama");
	}

	@Test
	public void updatePostDocumentWithRepository() throws Exception {
		// using postId 10 which is "Solr Rama"
		String postTitle = "updatePostDocumentWithRepository";
		repo.update(updatedPost(postTitle));
		PostDoc found = repo.findOne("10");
		assertEquals(found.getPostTitle(), postTitle);
	}

	@Test
	public void updatePostDocumentWithService() throws Exception {
		// using postId 10 which is "Solr Rama"
		String postTitle = "updatePostDocumentWithService";
		postDocService.updatePostDocument(updatedPost(postTitle));
		PostDoc found = repo.findOne("10");
		assertEquals(found.getPostTitle(), postTitle);
	}

	@Test
	public void cleanAndReindexPostDocuments() throws Exception {

		posts = postService.getAllPublishedPosts();
		postCount = posts.size();
		for (Post post : posts) {
			postDocService.addToIndex(post);
		}

		List<PostDoc> postDocs = postDocService.getAllPostDocuments();
		assertEquals(postDocs.size(), postCount);

		postDocs = postDocService.getPostsWithUserQuery("bootstrap");
		assertEquals(BOOTSTRAP_POST_COUNT, postDocs.size());

		Query query = new SimpleQuery(new SimpleStringCriteria("doctype:post"));
		solrOperations.delete(query);
		solrOperations.commit();
	}

	private Post updatedPost(String postTitle) throws PostNotFoundException {
		Post post = postService.getPostById(10L);
		PostDTO postDTO = PostUtils.postToPostDTO(post);
		postDTO.setPostTitle(postTitle);
		return postService.update(postDTO);
	}

	private Sort sortByIdDesc() {
		return new Sort(Sort.Direction.DESC, PostDoc.ID);
	}

}
