package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.enums.PostType;
import com.nixmash.springdata.jpa.exceptions.PostNotFoundException;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.service.PostService;
import com.nixmash.springdata.jpa.utils.PostUtils;
import com.nixmash.springdata.jsoup.service.JsoupService;
import com.nixmash.springdata.mvc.AbstractContext;
import com.nixmash.springdata.mvc.components.WebUI;
import com.nixmash.springdata.mvc.security.WithAdminUser;
import com.nixmash.springdata.mvc.security.WithPostUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static com.nixmash.springdata.mvc.controller.PostsController.*;
import static com.nixmash.springdata.mvc.security.SecurityRequestPostProcessors.csrf;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by daveburke on 5/27/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class PostsControllerTests extends AbstractContext {

    private PostsController mockPostsController;
    private static final String GOOD_URL = "http://nixmash.com/java/dysfunctional-enumerated-annotations-in-hibernate/";

    private MockMvc mockMvc;

    @Autowired
    WebUI webUI;

    @Autowired
    JsoupService jsoupService;

    @Autowired
    PostService postService;

    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setUp() {

        mockMvc = webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void homePageTest() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(view().name(POSTS_LIST_VIEW));
    }

    @Test
    public void postDisplayPage() throws Exception {
        mockMvc.perform(get("/posts/post/javascript-bootstrap"))
                .andExpect(model().attributeExists("post"))
                .andExpect(view().name(POSTS_PERMALINK_VIEW));
    }

    @Test(expected = PostNotFoundException.class)
    public void notFoundPostName_ThrowsPostNotFoundException() throws Exception {
        String badName = "bad-name";
        when(postService.getPost(badName))
                .thenThrow(new PostNotFoundException());

        mockMvc.perform(get("/posts/" + badName))
                .andExpect(status().isOk())
                .andExpect(view().name("errors/custom"));
    }

    @Test
    public void loadAddPostPage() throws Exception {
        mockMvc.perform(get("/posts/add"))
                .andExpect(model().attributeExists("postLink"))
                .andExpect(view().name(POSTS_ADD_VIEW));
    }

    @Test
    @WithPostUser
    public void getUpdatePostPage_Author_Loads() throws Exception {

        // h2 posts have keith userId (3) who is also in the ROLE_POST group and can create posts

        mockMvc.perform(get("/posts/update/3"))
                .andExpect(model().attributeExists("postDTO"))
                .andExpect(view().name(POSTS_UPDATE_VIEW));
    }

    @Test
    @WithAdminUser
    public void getUpdatePost_NonAuthor_403() throws Exception {

        mockMvc.perform(get("/posts/update/3"))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/403"));
    }

    @Test
    @WithAnonymousUser
    public void getUpdatePost_Anonymous_login() throws Exception {

        mockMvc.perform(get("/posts/update/3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/signin"));
    }

    @Test(expected = PostNotFoundException.class)
    public void badPostIdOnPostUpdate_ThrowsPostNotFoundException() throws Exception {

        when(postService.getPostById(-1L))
                .thenThrow(new PostNotFoundException());

        mockMvc.perform(get("/posts/update/-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("errors/custom"));
    }

    @Test
    public void updatePostWithMissingTitle_ReturnsToPage() throws Exception {
        RequestBuilder request = post("/posts/update")
                .param("postContent", "postContent").with(csrf());

        mockMvc.perform(request)
                .andExpect(model().attributeHasFieldErrors("postDTO", "postTitle"))
                .andExpect(view().name(POSTS_UPDATE_VIEW));
    }

    @Test
    public void updatePostWithValidData_RedirectsToPermalinkPage() throws Exception {

        String newTitle = "New Title for updatePostWithValidData_RedirectsToPermalinkPage Test";

        Post post = postService.getPostById(1L);
        RequestBuilder request = post("/posts/update")
                .param("postId", "1")
                .param("displayType", String.valueOf(post.getDisplayType()))
                .param("postContent", post.getPostContent())
                .param("postTitle", newTitle)
                .param("tags", "updatePostWithValidData1, updatePostWithValidData2")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("feedbackMessage"))
                .andExpect(redirectedUrl("/posts/post/" + PostUtils.createSlug(newTitle)));

        assert (post.getPostTitle().equals(newTitle));
    }

    @Test
    public void throwErrorOnEmptyPostLink() throws Exception {

        this.mockMvc.perform(get("/posts/add")
                .param("formtype", "link")
                .param("link", ""))
                .andExpect(status().isOk())
                .andExpect(model()
                        .attributeHasFieldErrorCode("postLink", "link", "post.link.is.empty"))
                .andExpect(view().name(POSTS_ADD_VIEW));
    }

    @Test
    public void showPostSourceContents() throws Exception {

        this.mockMvc.perform(get("/posts/add")
                .param("link", "http://bad.url")
                .param("formtype", "link"))
                .andExpect(status().isOk())
                .andExpect(model()
                        .attributeHasFieldErrorCode("postLink", "link", "post.link.page.not.found"))
                .andExpect(view().name(POSTS_ADD_VIEW));
    }

    @Test
    public void validLinkUrlDisplaysPagePreviewArea() throws Exception {
        this.mockMvc.perform(get("/posts/add")
                .param("formtype", "link")
                .param("link", GOOD_URL))
                .andExpect(status().isOk())
                .andExpect(model().attribute("showPost", "link"))
                .andExpect(view().name(POSTS_ADD_VIEW));
    }

    @Test
    public void noteSelectDisplaysNoteForm() throws Exception {
        this.mockMvc.perform(get("/posts/add")
                .param("formtype", "note"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("showPost", "note"))
                .andExpect(view().name(POSTS_ADD_VIEW));
    }

    @Test
    @WithPostUser
    public void submitNewNoteForm() throws Exception {
        mockMvc.perform(postRequest(PostType.NOTE, "submitNewNote"))
                .andExpect(model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("feedbackMessage"))
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    @WithPostUser
    public void newLinkPostAddsTwoNewTags() throws Exception {
        int tagStartCount = postService.getTagDTOs().size();
        mockMvc.perform(postRequest(PostType.LINK, "addsTwoTags"));
        int tagEndCount = postService.getTagDTOs().size();
        assertEquals(tagStartCount + 2, tagEndCount);
    }

    @Test
    @WithPostUser
    public void removingTagFromPostDecreasesItsTagCount() throws Exception {
        Post post = postService.getPostById(1L);
        int postTagStartCount = post.getTags().size();

        // tag size of Post 1L is 3. We are assigning a new tag, so the postTagEndCount
        // should be 2 less

        mockMvc.perform(post("/posts/update")
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

    @Test
    @WithUserDetails(value = "erwin")
    public void submitNewLinkFormAsNonAdmin() throws Exception {
        mockMvc.perform(postRequest(PostType.LINK, "postsAdd"))
                .andExpect(model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("feedbackMessage"))
                .andExpect(redirectedUrl("/posts/add"));
    }

    @Test
    @WithAdminUser
    public void submitNewLinkAsNonOwnerAdmin() throws Exception {
        mockMvc.perform(postRequest(PostType.LINK, "submitNewLink"))
                .andExpect(model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("feedbackMessage"))
                .andExpect(redirectedUrl("/posts"));
    }

    private RequestBuilder postRequest(PostType postType, String s) {
        return post("/posts/add")
                .param(postType.name().toLowerCase(), "true")
                .param("postTitle", "my title " + s)
                .param("postLink", "http://some.link/some/path")
                .param("postDescription", "my description")
                .param("postType", postType.name().toUpperCase())
                .param("displayType", postType.name().toUpperCase())
                .param("postContent", "My Post Content")
                .param("tags", String.format("req%s, req%s%s", s, s,1))
                .with(csrf());
    }

    private static ResultMatcher loginPage() {
        return result -> {
            status().isFound().match(result);
            redirectedUrl("http://localhost/signin").match(result);
        };
    }
}
