package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.enums.PostType;
import com.nixmash.springdata.jsoup.service.JsoupService;
import com.nixmash.springdata.mvc.AbstractContext;
import com.nixmash.springdata.mvc.components.WebUI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.nixmash.springdata.mvc.controller.PostsController.POSTS_ADD_VIEW;
import static com.nixmash.springdata.mvc.controller.PostsController.POSTS_LIST_VIEW;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Before
    public void setUp() {
        mockPostsController = new PostsController(webUI, jsoupService);
        mockMvc = MockMvcBuilders.standaloneSetup(mockPostsController).build();
    }

    @Test
    public void homePageTest() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(view().name(POSTS_LIST_VIEW));
    }

    @Test
    public void loadAddPostPage() throws Exception {
        mockMvc.perform(get("/posts/add"))
                .andExpect(model().attributeExists("postLink"))
                .andExpect(view().name(POSTS_ADD_VIEW));
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
    public void submitNewNoteForm() throws Exception {
        mockMvc.perform(postRequest(PostType.NOTE))
                .andExpect(model().attributeHasNoErrors())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("feedbackMessage"))
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    public void submitNewLinkForm() throws Exception {
        mockMvc.perform(postRequest(PostType.LINK))
                .andExpect(model().attributeHasNoErrors())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("feedbackMessage"))
                .andExpect(redirectedUrl("/posts"));
    }

    private RequestBuilder postRequest(PostType postType) {
        return post("/posts/add")
                .param(postType.name().toLowerCase(), "true")
                .param("postTitle", "my title")
                .param("postName", "my-title")
                .param("postContent", "My Post Content");
    }
}
