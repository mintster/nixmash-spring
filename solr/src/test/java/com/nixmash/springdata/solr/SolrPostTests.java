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
	private int postCount = 9;

	@Resource
	CustomPostDocRepository postDocRepository;

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
		postDocRepository.save(postDoc);

		PostDoc found = postDocRepository.findOne("10");
		assertEquals(found.getPostName(), "solr-rama");
		postDocRepository.delete("10");
	}

	@Test
	public void addPostWithAddToIndexService() throws Exception {
		// using postId 10 which is "Solr Rama"
		Post post = postService.getPostById(10L);
		postDocService.addToIndex(post);
		PostDoc found = postDocRepository.findOne("10");
		System.out.println(found);
		assertEquals(found.getPostName(), "solr-rama");
	}

	@Test
	public void updatePostDocumentWithRepository() throws Exception {
		// using postId 10 which is "Solr Rama"
		String postTitle = "updatePostDocumentWithRepository";
		postDocRepository.update(updatedPost(postTitle));
		PostDoc found = postDocRepository.findOne("10");
		assertEquals(found.getPostTitle(), postTitle);
	}

	@Test
	public void updatePostDocumentWithService() throws Exception {
		// using postId 10 which is "Solr Rama"
		String postTitle = "updatePostDocumentWithService";
		postDocService.updatePostDocument(updatedPost(postTitle));
		PostDoc found = postDocRepository.findOne("10");
		assertEquals(found.getPostTitle(), postTitle);
	}

	@Test
	public void searchPostsWithCriteria() throws Exception {
		List<PostDoc> postDocs = postDocService.doQuickSearch("post ways");
//		List<PostDoc> postDocs = postDocService.getAllPostDocuments();

		//  1) Each of the search terms must be in either the Post Title or Body
		//  2) All of the search terms must be present in Post

		// Query: (Title.contains("post") or Body.contains("post")) AND (Title.contains("ways") or Body.contains("ways"))


		for (PostDoc postDoc: postDocs) {
			assert(postDoc.getPostTitle().toLowerCase().contains("post") ||
					postDoc.getPostText().toLowerCase().contains("post"));

			assert(postDoc.getPostTitle().toLowerCase().contains("ways") ||
					postDoc.getPostText().toLowerCase().contains("ways"));

			System.out.println(postDoc.getPostTitle() + " | " + postDoc.getPostText() + " | " + postDoc.getPostDate());
		}
	}

	@Test
	public void quickSearchListSize_IsZero_OnNoResults() throws Exception {
		List<PostDoc> postDocs = postDocService.doQuickSearch("no_results_to_find_here");
		assertEquals(postDocs.size(), 0);
	}

	private Post updatedPost(String postTitle) throws PostNotFoundException {
		Post post = postService.getPostById(10L);
		PostDTO postDTO = PostUtils.postToPostDTO(post);
		postDTO.setPostTitle(postTitle);
		return postService.update(postDTO);
	}

}
