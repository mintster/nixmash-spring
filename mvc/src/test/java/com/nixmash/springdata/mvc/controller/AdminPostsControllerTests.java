package com.nixmash.springdata.mvc.controller;

import com.github.dandelion.core.web.DandelionFilter;
import com.nixmash.springdata.jpa.exceptions.PostNotFoundException;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.model.Tag;
import com.nixmash.springdata.jpa.service.PostService;
import com.nixmash.springdata.jpa.utils.PostUtils;
import com.nixmash.springdata.mvc.AbstractContext;
import com.nixmash.springdata.mvc.components.WebUI;
import com.nixmash.springdata.mvc.security.WithAdminUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletException;
import java.util.ArrayList;

import static com.nixmash.springdata.mvc.controller.AdminPostsController.*;
import static com.nixmash.springdata.mvc.security.SecurityRequestPostProcessors.csrf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringJUnit4ClassRunner.class)
@WithAdminUser
public class AdminPostsControllerTests extends AbstractContext {

    private static final String POST_CONSTANT = "POST";
    private DandelionFilter dandelionFilter;

    private static final String GOOD_URL = "http://nixmash.com/java/dysfunctional-enumerated-annotations-in-hibernate/";

    @Autowired
    private WebUI webUI;

    @Autowired
    private PostService postService;

    private MockMvc mvc;

    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() throws ServletException {

        this.dandelionFilter = new DandelionFilter();
        this.dandelionFilter.init(new MockFilterConfig());

        mvc = webAppContextSetup(wac)
                .apply(springSecurity())
                .addFilters(dandelionFilter)
                .build();

//        postService = mock(PostService.class);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void postsList_page_loads() throws Exception {
        RequestBuilder request = get("/admin/posts").with(csrf());
        MvcResult result = mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(ADMIN_POSTS_LIST_VIEW)).andReturn();

        assertThat(result.getModelAndView().getModel().get("posts"),
                is(instanceOf(ArrayList.class)));
    }

    // region Posts

    @Test
    public void addPost_page_loads() throws Exception {
        RequestBuilder request = get("/admin/posts/add/post").with(csrf());
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("postDTO"))
                .andExpect(view().name(ADMIN_POST_ADD_VIEW));

    }

    @Test
    public void updatePostWithMissingTitle_ReturnsToPage() throws Exception {
        RequestBuilder request = post("/admin/posts/update")
                .param("postContent", "postContent").with(csrf());

        mvc.perform(request)
                .andExpect(model().attributeHasFieldErrors("postDTO", "postTitle"))
                .andExpect(view().name(ADMIN_POSTLINK_UPDATE_VIEW));
    }

    @Test
    public void updatePostWithValidData_RedirectsToPermalinkPage() throws Exception {

        String newTitle = "New Title for updatePostWithValidData_RedirectsToPermalinkPage Test";

        Post post = postService.getPostById(1L);
        RequestBuilder request = post("/admin/posts/update")
                .param("postId", "1")
                .param("displayType", String.valueOf(post.getDisplayType()))
                .param("postContent", post.getPostContent())
                .param("postTitle", newTitle)
                .param("tags", "updatePostWithValidData1, updatePostWithValidData2")
                .with(csrf());

        mvc.perform(request)
                .andExpect(model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("feedbackMessage"))
                .andExpect(redirectedUrl("/posts/post/" + PostUtils.createSlug(newTitle)));

        assert (post.getPostTitle().equals(newTitle));
    }

    @Test(expected = PostNotFoundException.class)
    public void badPostIdOnPostUpdate_ThrowsPostNotFoundException() throws Exception {

        when(postService.getPostById(-2L))
                .thenThrow(new PostNotFoundException());

        mvc.perform(get("/admin/posts/update/-2"))
                .andExpect(status().isOk())
                .andExpect(view().name("errors/custom"));
    }

    @Test
    public void addsTwoNewTags() throws Exception {
        int tagStartCount = postService.getTagDTOs().size();
        mvc.perform(addPostRequest("addsTwoTags"));
        int tagEndCount = postService.getTagDTOs().size();
        assertEquals(tagStartCount + 2, tagEndCount);
    }

    @Test
    public void addNewPostRecord() throws Exception {
        int postStartCount = postService.getAllPosts().size();
        mvc.perform(addPostRequest("addNewPostRecord"));
        int postEndCount = postService.getAllPosts().size();
        assertEquals(postStartCount + 1, postEndCount);
    }

    @Test
    public void newPublishedPostRedirectsToPostList() throws Exception {
        mvc.perform(addPostRequest("redirectsToPostList"))
                .andExpect(redirectedUrl("/admin/posts"));
    }

    @Test
    public void newUnpublishedPostReturnsPostAddView() throws Exception {
        mvc.perform(addPostRequest("returnsPostAddView", false))
                .andExpect(view().name(ADMIN_POST_ADD_VIEW));
    }

    @Test
    public void removingTagFromPostDecreasesItsTagCount() throws Exception {
        Post post = postService.getPostById(1L);
        int postTagStartCount = post.getTags().size();

        // tag size of Post 1L is 3. We are assigning a new tag, so the postTagEndCount
        // should be 2 less

        mvc.perform(post("/admin/posts/update")
                .param("postId", "1")
                .param("displayType", String.valueOf(post.getDisplayType()))
                .param("postContent", post.getPostContent())
                .param("postTitle", post.getPostTitle())
                .param("tags", "removingTag1")
                .with(csrf()));

        int postTagEndCount = post.getTags().size();
        assertEquals(postTagEndCount, postTagStartCount - 2);

        Post verifyPost = postService.getPostById(1L);
        assertEquals(verifyPost.getTags().size(), postTagEndCount);

    }

    // endregion

    // region Links

    @Test
    public void addLink_page_loads() throws Exception {
        RequestBuilder request = get("/admin/posts/add/link").with(csrf());
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("postDTO"))
                .andExpect(view().name(ADMIN_LINK_ADD_VIEW));
    }

    @Test
    public void addLink_page_loads_with_Url() throws Exception {
        mvc.perform(get("/admin/posts/add/link")
                .param("isLink", "true")
                .param("link", GOOD_URL).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("hasLink"))
                .andExpect(view().name(ADMIN_LINK_ADD_VIEW));
    }

    @Test
    public void addLink_page_returns_error_on_missing_Url() throws Exception {
        mvc.perform(get("/admin/posts/add/link")
                .param("isLink", "true")
                .param("link", "").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model()
                        .attributeHasFieldErrorCode("postLink", "link", "post.link.is.empty"))
                .andExpect(view().name(ADMIN_LINK_ADD_VIEW));
    }

    @Test
    public void throwErrorOnEmptyPostLink() throws Exception {

        mvc.perform(get("/admin/posts/add/link")
                .param("isLink", "true")
                .param("link", ""))
                .andExpect(status().isOk())
                .andExpect(model()
                        .attributeHasFieldErrorCode("postLink", "link", "post.link.is.empty"))
                .andExpect(view().name(ADMIN_LINK_ADD_VIEW));
    }

    @Test
    public void updatePostLink_page_loads() throws Exception {
        RequestBuilder request = get("/admin/posts/update/1").with(csrf());
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("postDTO"))
                .andExpect(view().name(ADMIN_POSTLINK_UPDATE_VIEW));
    }

    // endregion

    // region Tags

    @Test
    public void tagsList_page_loads() throws Exception {
        RequestBuilder request = get("/admin/posts/tags").with(csrf());
        MvcResult result = mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(ADMIN_TAGS_VIEW)).andReturn();

        assertThat(result.getModelAndView().getModel().get("tags"),
                is(instanceOf(ArrayList.class)));
    }

    @Test
    public void addTag_increases_tag_count() throws Exception {
        int preTagCount = postService.getTagDTOs().size();
        mvc.perform(addTagRequest("tagIncreasesTagCount"))
                .andExpect(redirectedUrl("/admin/posts/tags"));

        int postTagCount = postService.getTagDTOs().size();
        assertThat(postTagCount, is(greaterThan(preTagCount)));
    }

    @Test
    public void updateTag_changes_tag_value() throws Exception {
        Tag preTag = postService.getTag("h2tagfour");

        mvc.perform(updateTagRequest(preTag.getTagId(), "updateChangesTagName"))
                .andExpect(redirectedUrl("/admin/posts/tags"));

        Tag postTag = postService.getTag("updateChangesTagName");
        assertEquals(preTag.getTagId(), postTag.getTagId());
    }

    @Test
    public void deleteTag_decreases_tag_count() throws Exception {
        int preTagCount = postService.getTagDTOs().size();
        Tag preTag = postService.getTag("h2tagsix");

        mvc.perform(deleteTagRequest(preTag.getTagId()))
                .andExpect(redirectedUrl("/admin/posts/tags"));

        int postTagCount = postService.getTagDTOs().size();
        assertThat(postTagCount, is(lessThan(preTagCount)));
    }

    // endregion

    // region Utility Methods

    private RequestBuilder addPostRequest(String s) {
        return addPostRequest(s, true);
    }

    private RequestBuilder addPostRequest(String s, Boolean isPublished) {
        return post("/admin/posts/add/post")
                .param("post", isPublished ? POST_PUBLISH : POST_DRAFT)
                .param("postTitle", "my title " + s)
                .param("hasPost", "true")
                .param("postLink", "http://some.link/some/path")
                .param("postDescription", "my description")
                .param("postType", POST_CONSTANT)
                .param("displayType", POST_CONSTANT)
                .param("postContent", "My Post Content")
                .param("isPublished", "true")
                .param("tags", String.format("req%s, req%s%s", s, s, 1))
                .with(csrf());
    }

    private RequestBuilder addTagRequest(String s) {
        return post("/admin/posts/tags/new")
                .param("tagValue", s)
                .with(csrf());
    }

    private RequestBuilder updateTagRequest(long tagId, String s) {
        return post("/admin/posts/tags/update")
                .param("tagValue", s)
                .param("tagId", String.valueOf(tagId))
                .with(csrf());
    }

    private RequestBuilder deleteTagRequest(long tagId) {
        return post("/admin/posts/tags/update")
                .param("deleteTag", "true")
                .param("tagId", String.valueOf(tagId))
                .with(csrf());
    }
    // endregion

}
