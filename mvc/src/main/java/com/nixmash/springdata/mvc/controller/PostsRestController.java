package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.common.ApplicationSettings;
import com.nixmash.springdata.jpa.dto.PostQueryDTO;
import com.nixmash.springdata.jpa.dto.TagDTO;
import com.nixmash.springdata.jpa.enums.PostDisplayType;
import com.nixmash.springdata.jpa.enums.PostType;
import com.nixmash.springdata.jpa.exceptions.PostNotFoundException;
import com.nixmash.springdata.jpa.model.CurrentUser;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.service.PostService;
import com.nixmash.springdata.jpa.utils.Pair;
import com.nixmash.springdata.jpa.utils.PostUtils;
import com.nixmash.springdata.mail.service.FmService;
import com.nixmash.springdata.mvc.annotations.JsonRequestMapping;
import com.nixmash.springdata.solr.model.PostDoc;
import com.nixmash.springdata.solr.service.PostDocService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.data.solr.UncategorizedSolrException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;

import static com.nixmash.springdata.mvc.controller.PostsController.*;


@RestController
@JsonRequestMapping(value = "/json/posts")
public class PostsRestController {

    private static final Logger logger = LoggerFactory.getLogger(PostsRestController.class);
    private static final String TITLE_TEMPLATE = "title";
    private static final String SESSION_ATTRIBUTE_POSTS = "posts";
    private static final String SESSION_ATTRIBUTE_TAGPOSTTITLES = "tagposttitles";
    private static final String SESSION_ATTRIBUTE_POSTTITLES = "posttitles";
    private static final String SESSION_ATTRIBUTE_TAGGEDPOSTS = "taggedposts";
    private static final String SESSION_ATTRIBUTE_LIKEDPOSTS = "likedposts";
    private static final String SESSION_ATTRIBUTE_JUSTLINKS = "justlinks";
    private static final String SESSION_ATTRIBUTE_QUICKSEARCH_POSTS = "quicksearchposts";
    private static final String SESSION_ATTRIBUTE_FULLSEARCH_POSTS = "fullsearchposts";

    private PostService postService;
    private FmService fmService;
    private ApplicationSettings applicationSettings;
    private PostDocService postDocService;

    private int minTagCount = 0;
    private int maxTagCount = 0;

    @Autowired
    public PostsRestController(PostService postService, FmService fmService, ApplicationSettings applicationSettings, PostDocService postDocService) {
        this.postService = postService;
        this.fmService = fmService;
        this.applicationSettings = applicationSettings;
        this.postDocService = postDocService;
    }

    // region Post Titles

    @RequestMapping(value = "/titles/page/{pageNumber}", produces = "text/html;charset=UTF-8")
    public String getPostTitles(@PathVariable Integer pageNumber, HttpServletRequest request, CurrentUser currentUser) {
        Slice<Post> posts = postService.getPublishedPosts(pageNumber, TITLE_PAGING_SIZE);
        String result = populatePostStream(posts.getContent(), currentUser, TITLE_TEMPLATE);
        WebUtils.setSessionAttribute(request, SESSION_ATTRIBUTE_POSTTITLES, posts.getContent());
        return result;
    }

    @RequestMapping(value = "/titles/more")
    public String getTitleHasNext(HttpServletRequest request) {
        return hasNext(request, SESSION_ATTRIBUTE_POSTTITLES, TITLE_PAGING_SIZE);
    }

    // endregion

    // region Just Links

    @RequestMapping(value = "/links/page/{pageNumber}", produces = "text/html;charset=UTF-8")
    public String getLinks(@PathVariable Integer pageNumber, HttpServletRequest request, CurrentUser currentUser) {
        Slice<Post> posts = postService.getPagedPostsByPostType(PostType.LINK, pageNumber, POST_PAGING_SIZE);
        String result = populatePostStream(posts.getContent(), currentUser);
        WebUtils.setSessionAttribute(request, SESSION_ATTRIBUTE_JUSTLINKS, posts.getContent());
        return result;
    }

    @RequestMapping(value = "/links/more")
    public String getLinksHasNext(HttpServletRequest request) {
        return hasNext(request, SESSION_ATTRIBUTE_JUSTLINKS, POST_PAGING_SIZE);
    }

    // endregion

    // region Posts by Tag

    @RequestMapping(value = "/titles/tag/{tagid}/page/{pageNumber}",
            produces = "text/html;charset=UTF-8")
    public String getPostTitlesByTagId(@PathVariable long tagid,
                                       @PathVariable int pageNumber,
                                       HttpServletRequest request,
                                       CurrentUser currentUser) {
        Slice<Post> posts = postService.getPostsByTagId(tagid, pageNumber, TITLE_PAGING_SIZE);
        String result = populatePostStream(posts.getContent(), currentUser, TITLE_TEMPLATE);
        WebUtils.setSessionAttribute(request, SESSION_ATTRIBUTE_TAGPOSTTITLES, posts.getContent());
        return result;
    }


    @RequestMapping(value = "/titles/tag/{tagid}/more")
    public String getTagTitlesHasNext(@PathVariable int tagid, HttpServletRequest request) {
        return hasNext(request, SESSION_ATTRIBUTE_TAGPOSTTITLES, TITLE_PAGING_SIZE);
    }

    // endregion

    // region Posts

    @RequestMapping(value = "/page/{pageNumber}", produces = "text/html;charset=UTF-8")
    public String getPosts(@PathVariable Integer pageNumber, HttpServletRequest request, CurrentUser currentUser) {
        Slice<Post> posts = postService.getPublishedPosts(pageNumber, POST_PAGING_SIZE);
        String result = populatePostStream(posts.getContent(), currentUser);
        WebUtils.setSessionAttribute(request, SESSION_ATTRIBUTE_POSTS, posts.getContent());
        return result;
    }


    @RequestMapping(value = "/more")
    public String getHasNext(HttpServletRequest request) {
        return hasNext(request, SESSION_ATTRIBUTE_POSTS, POST_PAGING_SIZE);
    }

    // endregion

    // region Posts by Tag

    @RequestMapping(value = "/tag/{tagid}/page/{pageNumber}",
            produces = "text/html;charset=UTF-8")
    public String getPostsByTagId(@PathVariable long tagid,
                                  @PathVariable int pageNumber,
                                  HttpServletRequest request,
                                  CurrentUser currentUser) {
        Slice<Post> posts = postService.getPostsByTagId(tagid, pageNumber, POST_PAGING_SIZE);
        String result = populatePostStream(posts.getContent(), currentUser);
        WebUtils.setSessionAttribute(request, SESSION_ATTRIBUTE_TAGGEDPOSTS, posts.getContent());
        return result;
    }

    @RequestMapping(value = "/tag/{tagid}/more")
    public String getHasNext(@PathVariable int tagid, HttpServletRequest request) {
        return hasNext(request, SESSION_ATTRIBUTE_TAGGEDPOSTS, POST_PAGING_SIZE);
    }

    // endregion

    // region Quick Search


    @RequestMapping(value = "/quicksearch/page/{pageNumber}",
            produces = "text/html;charset=UTF-8")
    public String getQuickSearchPosts(@PathVariable int pageNumber,
                                      HttpServletRequest request,
                                      CurrentUser currentUser) {
        String search = (String) WebUtils.getSessionAttribute(request, SESSION_QUICKSEARCH_QUERY);
        String result;
        List<PostDoc> postDocs = postDocService.doQuickSearch(search);
        if (postDocs.size() == 0) {
            result = fmService.getNoResultsMessage(search);
        } else {
            Slice<PostDoc> posts = postDocService.doPagedQuickSearch(search, pageNumber, POST_PAGING_SIZE);
            result = populatePostDocStream(posts.getContent(), currentUser);
            WebUtils.setSessionAttribute(request, SESSION_ATTRIBUTE_QUICKSEARCH_POSTS, posts.getContent());
        }
        return result;
    }

    @RequestMapping(value = "/quicksearch/more")
    public String getQuickSearchHasNext(HttpServletRequest request) {
        return hasNext(request, SESSION_ATTRIBUTE_QUICKSEARCH_POSTS, POST_PAGING_SIZE);
    }

    // endregion

    // region Full Search


    @RequestMapping(value = "/search/page/{pageNumber}",
            produces = "text/html;charset=UTF-8")
    public String getFullSearchPosts(@PathVariable int pageNumber,
                                     HttpServletRequest request,
                                     CurrentUser currentUser) {
        PostQueryDTO postQueryDTO =
                (PostQueryDTO) WebUtils.getSessionAttribute(request, SESSION_POSTQUERYDTO);
        String result = null;
        List<PostDoc> postDocs = null;
        if (postQueryDTO != null) {
            try {
                postDocs = postDocService.doFullSearch(postQueryDTO);
            } catch (UncategorizedSolrException ex) {
                logger.info(MessageFormat.format("Bad Query: {0}", postQueryDTO.getQuery()));
                return fmService.getNoResultsMessage(postQueryDTO.getQuery());
            }

            if (postDocs.size() == 0) {
                result = fmService.getNoResultsMessage(postQueryDTO.getQuery());
            } else {
                Slice<PostDoc> postDocSlice =
                        postDocService.doPagedFullSearch(postQueryDTO, pageNumber, POST_PAGING_SIZE);
                result = populatePostDocStream(postDocSlice.getContent(), currentUser);
                WebUtils.setSessionAttribute(request,
                        SESSION_ATTRIBUTE_FULLSEARCH_POSTS, postDocSlice.getContent());
            }
        }
        return result;
    }

    @RequestMapping(value = "/search/more")
    public String getFullSearchHasNext(HttpServletRequest request) {
        return hasNext(request, SESSION_ATTRIBUTE_FULLSEARCH_POSTS, POST_PAGING_SIZE);
    }

    // endregion

    // region Likes

    @RequestMapping(value = "/post/like/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public int likePost(@PathVariable("postId") int postId, CurrentUser currentUser) {
        return postService.addPostLike(currentUser.getId(), postId);
    }

    @RequestMapping(value = "/likes/{userId}/page/{pageNumber}",
            produces = "text/html;charset=UTF-8")
    public String getPostsByLikes(@PathVariable long userId,
                                  @PathVariable int pageNumber,
                                  HttpServletRequest request,
                                  CurrentUser currentUser) {
        List<Post> posts = postService.getPostsByUserLikes(userId);
        String result;
        if (posts == null) {
            result = fmService.getNoLikesMessage();
        } else {
            posts = postService.getPagedLikedPosts(userId, pageNumber, POST_PAGING_SIZE);
            result = populatePostStream(posts, currentUser);
            WebUtils.setSessionAttribute(request, SESSION_ATTRIBUTE_LIKEDPOSTS, posts);
        }
        return result;
    }

    @RequestMapping(value = "/likes/{userId}/more")
    public String getHasNextLike(@PathVariable int userId, HttpServletRequest request) {
        return hasNext(request, SESSION_ATTRIBUTE_LIKEDPOSTS, POST_PAGING_SIZE);
    }

    // endregion

    // region Shared Utils

    @SuppressWarnings("unchecked")
    private String hasNext(HttpServletRequest request, String attribute, int pagingSize) {
        List<Post> posts = (List<Post>) WebUtils.getSessionAttribute(request, attribute);
        if (posts != null)
            return Boolean.toString(posts.size() >= pagingSize);
        else
            return "true";
    }

    private String populatePostStream(List<Post> posts, CurrentUser currentUser) {
        return populatePostStream(posts, currentUser, null);
    }

    private String populatePostDocStream(List<PostDoc> postDocs, CurrentUser currentUser) {
        List<Post> posts = new ArrayList<>();
        for (PostDoc postDoc :
                postDocs) {
            try {
                posts.add(postService.getPostById(Long.parseLong(postDoc.getPostId())));
            } catch (PostNotFoundException e) {
                logger.info("Could not convert PostDoc {} to Post with title \"{}\"", postDoc.getPostId(), postDoc.getPostTitle());
            }
        }
        return populatePostStream(posts, currentUser, null);
    }

    private String populatePostStream(List<Post> posts, CurrentUser currentUser, String format) {
        String result = StringUtils.EMPTY;
        for (Post post : posts) {
            try {
                if (post.getDisplayType().equals(PostDisplayType.MULTIPHOTO_POST)) {
                    post.setPostImages(postService.getPostImages(post.getPostId()));
                }
                if (post.getDisplayType().equals(PostDisplayType.SINGLEPHOTO_POST)) {
                    post.setSingleImage(postService.getPostImages(post.getPostId()).get(0));
                }
            } catch (Exception e) {
                logger.info(String.format("Image Retrieval Error for Post ID:%s Title: %s", String.valueOf(post.getPostId()), post.getPostTitle()));
            }
            post.setIsOwner(PostUtils.isPostOwner(currentUser, post.getUserId()));
            result += fmService.createPostHtml(post, format);
        }
        return result;
    }

    // endregion

    // region get Tags

    @RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<TagDTO> getAllTagDTOs() {
        return postService.getTagDTOs();
    }

    @RequestMapping(value = "/tagvalues", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getTagValues() {
        return postService.getTagValues();
    }


    @RequestMapping(value = "/tagcloud", produces = "text/html;charset=UTF-8")
    public String getSidebarTagCloud() {

        int tagCount = applicationSettings.getSidebarTagCloudCount();
        List<TagDTO> tags = postService.getTagCloud(tagCount);
        maxTagCount = tags.stream().mapToInt(TagDTO::getTagCount).max().orElse(0);
        minTagCount = tags.stream().mapToInt(TagDTO::getTagCount).min().orElse(0);

        StringBuilder tagHtml = new StringBuilder();
        tagHtml.append("<ul class='taglist'>");
        for (TagDTO tag : tags) {
            tagHtml.append(tagHtml(tag));
        }
        tagHtml.append("</ul>");
        return tagHtml.toString();
    }

    private String tagHtml(TagDTO tag) {
        String tagPattern = "<li><a href='%s/posts/tag/%s' class='%s'>%s</a></li>";
        String cssClass = getCssTag(tag.getTagCount());
        String tagLowerCase = tag.getTagValue().toLowerCase();

        return String.format(tagPattern,
                applicationSettings.getBaseUrl(), tagLowerCase, cssClass, tag.getTagValue());
    }

    // endregion

    // region Tag Cloud

    private String getCssTag(int tagCount) {

        String cssClass = "smallTag";
        int diff = maxTagCount - minTagCount;
        int distribution = diff / 5;

        if (tagCount == maxTagCount)
            cssClass = "maxTag";
        else if (tagCount == minTagCount)
            cssClass = "minTag";
        else if (tagCount > (minTagCount + (distribution * 1.75)))
            cssClass = "largeTag";
        else if (tagCount > (minTagCount + distribution))
            cssClass = "mediumTag";

        return cssClass;
    }

    // endregion

    // region Key-Value Json

    //
    // --- demo for NixMash Post "Variations on JSON Key-Value Pairs in Spring MVC"  http://goo.gl/0hhnZg

    private String key = "key";
    private String value = "Json Key-Value Demo";

    /*
    *           Returns:  {  "key" : "Json Key-Value Demo"  }
     */
    @RequestMapping(value = "/map", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> returnMap() {
        Map<String, String> keyvalues = new HashMap<>();
        keyvalues.put(key, value);
        return keyvalues;
    }

    /*
    *           Returns:  {  "key" : "Json Key-Value Demo"  }
     */
    @RequestMapping(value = "/simpleentry")
    public SimpleEntry<String, String> returnSimpleEntry() {
        return new SimpleEntry<>(key, value);
    }

    /*
    *           Returns:  {  "key" : "Json Key-Value Demo"  }
     */
    @RequestMapping(value = "/singleton")
    public Map<String, String> returnSingletonMapFromCollection() {
        return Collections.singletonMap(key, value);
    }

    /*
    *           Returns:
    *           {
    *                    "key" : "key",
    *                     "value" : "Json Key-Value Demo"
    *           }
     */
    @RequestMapping(value = "/pair")
    public Pair<String, String> returnPair() {
        return new Pair<>(key, value);
    }

    // endregion

}
