package com.nixmash.springdata.mail;

import com.nixmash.springdata.jpa.exceptions.PostNotFoundException;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.jpa.service.PostService;
import com.nixmash.springdata.jpa.service.UserService;
import com.nixmash.springdata.mail.service.FmService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

@RunWith(SpringRunner.class)
public class FmServiceTests extends MailContext {

    private User user;
    private Post post;
    private Post link;
    private String postTitle;
    private String linkTitle;
    private String siteName;

    @Autowired
    Environment environment;

    @Autowired
    FmService fmService;

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Before
    public void setup() throws PostNotFoundException {
        user = userService.getUserByUsername("erwin");                            // User:  Erwin Lapote

        post = postService.getPostById(1L);                                                         // Post Title: Freestanding Note Post
        postTitle = post.getPostTitle();

        link = postService.getPostById(3L);                                                           // Link Post Type (no image): Jsoup Parsing and Traversing Document and URL - JAVATIPS.INFO
        linkTitle = link.getPostTitle();

        siteName = environment.getProperty("mail.site.name");           // NixMash Spring
    }

    // region Test Template

    @Test
    public void testTemplate()  {
        String result = fmService.displayTestTemplate(user);
        assertThat(result, containsString(siteName));
    }

    // endregion

    // region Posts

    @Test
    public void noLikesTemplate()   {
        String result = fmService.getNoLikesMessage();
        assertThat(result, containsString("No Liked Posts Selected"));
    }

    @Test
    public void flashcardPostTemplate()  {
        String result = fmService.createPostHtml(post, "flashcard_post");
        assertThat(result, containsString(postTitle));
    }

    @Test
    public void postTemplate()  {
        String result = fmService.createPostHtml(post);
        assertThat(result, containsString(postTitle));
    }

    @Test
    public void titlePostTemplate()  {
        String result = fmService.createPostHtml(post, "title");
        assertThat(result, containsString(postTitle));
    }

    @Test
    public void multiphotoPostTemplate()  {
        String result = fmService.createPostHtml(post, "multiphoto_post");
        assertThat(result, containsString(postTitle));
    }

    @Test
    public void singlephotoPostTemplate()  {
        String result = fmService.createPostHtml(post, "singlephoto_post");
        assertThat(result, containsString(postTitle));
    }

    // endregion

    // region LInk Templates

    @Test
    public void linkTemplate()  {
        String result = fmService.createPostHtml(link, "link");
        assertThat(result, containsString(linkTitle));
    }

    @Test
    public void linkSummaryTemplate()  {
        String result = fmService.createPostHtml(link, "link_summary");
        assertThat(result, containsString(linkTitle));
    }

    @Test
    public void linkFeatureTemplate()  {
        String result = fmService.createPostHtml(link, "link_feature");
        assertThat(result, containsString(linkTitle));
    }

    @Test
    public void nixmashLinkTemplate()  {
        String result = fmService.createPostHtml(link, "nixmash_post");
        assertThat(result, containsString(linkTitle));
    }

    // endregion

    // region Utility Templates

    @Test
    public void robotsTxtTemplate()  {
        String result = fmService.getRobotsTxt();
        assertThat(result, containsString("User-agent"));
    }

    @Test
    public void fileUploadedTemplate()  {
        String result = fmService.getFileUploadedScript();
        assertThat(result, containsString("template-download"));
    }

    @Test
    public void fileUploadingTemplate()  {
        String result = fmService.getFileUploadingScript();
        assertThat(result, containsString("template-upload"));
    }

    // endregion

}
