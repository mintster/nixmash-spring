package com.nixmash.springdata.solr;

import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.dto.PostQueryDTO;
import com.nixmash.springdata.jpa.enums.PostType;
import com.nixmash.springdata.jpa.exceptions.PostNotFoundException;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.service.PostService;
import com.nixmash.springdata.jpa.utils.PostUtils;
import com.nixmash.springdata.solr.model.PostDoc;
import com.nixmash.springdata.solr.repository.custom.CustomPostDocRepository;
import com.nixmash.springdata.solr.service.PostDocService;
import com.nixmash.springdata.solr.utils.SolrUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.UncategorizedSolrException;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
public class SolrPostTests extends SolrContext {

	@Resource
	CustomPostDocRepository postDocRepository;

	@Autowired
	private PostDocService postDocService;

	@Autowired
	PostService postService;

	@Autowired
	SolrOperations solrOperations;

	@Before
	public void setupSolr() {
		Query query = new SimpleQuery(new SimpleStringCriteria("doctype:post"));
		solrOperations.delete(query);
		solrOperations.commit();
		List<Post> posts = postService.getAllPublishedPosts();
		postDocService.addAllToIndex(posts);
	}

	@Test
	public void queryForPage() {
		int postCount = postService.getAllPublishedPosts().size();
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
	public void findPostDocByPostIdNotNullWithRepository() throws Exception {
		PostDoc postDoc = postDocRepository.findPostDocByPostId(1L);
		assertNotNull(postDoc);
	}

	@Test
	public void findPostDocByPostIdNotNullWithService() throws Exception {
		PostDoc postDoc = postDocService.getPostDocByPostId(1L);
		assertNotNull(postDoc);
	}

	@Test
	public void notFoundPostDocByPostIdIsNull() throws Exception {
		PostDoc postDoc = postDocService.getPostDocByPostId(1000L);
		assertNull(postDoc);
	}

	@Test
	public void addPostWithAddToIndexService() throws Exception {
		// using postId 10 which is "Solr Rama"
		Post post = postService.getPostById(10L);
		postDocService.addToIndex(post);
		PostDoc found = postDocRepository.findOne("10");
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

	// region fullSearch tests

	// fullSearch tests use the following PostDocument objects

		//100 Ways To Title Something
		//This post title begins with 100 : POST
		//------------------------
		//200 Ways To Title Something
		//This post title begins with 200 : POST
		//------------------------
		//1000 Ways To Title Something
		//This post title begins with 1000 : POST

	@Test
	public void fullSearch() throws Exception {
		PostQueryDTO postQueryDTO = new PostQueryDTO("body:begins", PostType.UNDEFINED);
		List<PostDoc> postDocs = postDocService.doFullSearch(postQueryDTO);
		assertEquals(postDocs.size(), 3);
	}

	@Test
	public void fullSearchWithPostType_POST_ReturnsAll() throws Exception {
		PostQueryDTO postQueryDTO = new PostQueryDTO("body:begins", PostType.POST);
		List<PostDoc> postDocs = postDocService.doFullSearch(postQueryDTO);
		assertEquals(postDocs.size(), 3);
	}

	@Test
	public void fullSearchWithPostType_LINK_ReturnsNone() throws Exception {
		PostQueryDTO postQueryDTO = new PostQueryDTO("body:begins", PostType.LINK);
		List<PostDoc> postDocs = postDocService.doFullSearch(postQueryDTO);
		assertEquals(postDocs.size(), 0);
	}

	@Test
	public void badSimpleQueryThrowsUncategorizedSolrException() {
		int i = 0;
		try {
			postDocService.doFullSearch(new PostQueryDTO("bad:field"));
		} catch (Exception ex) {
			i++;
			Assert.assertTrue(ex instanceof UncategorizedSolrException);
		}
		try {
			postDocService.doFullSearch(new PostQueryDTO("bad::format"));
		} catch (Exception ex) {
			i++;
			Assert.assertTrue(ex instanceof UncategorizedSolrException);
		}
		try {
			postDocService.doFullSearch(new PostQueryDTO("title:goodquery"));
		} catch (UncategorizedSolrException ex) {
			i++;
		}
		Assert.assertEquals(2, i);
	}

	// endregion


	@Test
	public void allPostDocuments() {
		List<PostDoc> postDocs = postDocService.getAllPostDocuments();
		SolrUtils.printPostDocs(postDocs);
	}

	private Post updatedPost(String postTitle) throws PostNotFoundException {
		Post post = postService.getPostById(10L);
		PostDTO postDTO = PostUtils.postToPostDTO(post);
		postDTO.setPostTitle(postTitle);
		return postService.update(postDTO);
	}

}
