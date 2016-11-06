package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.dto.AlphabetDTO;
import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.dto.TagDTO;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.jpa.enums.PostType;
import com.nixmash.springdata.jpa.exceptions.DuplicatePostNameException;
import com.nixmash.springdata.jpa.exceptions.PostNotFoundException;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.model.PostImage;
import com.nixmash.springdata.jpa.repository.LikeRepository;
import com.nixmash.springdata.jpa.utils.PostTestUtils;
import com.nixmash.springdata.jpa.utils.PostUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static com.nixmash.springdata.jpa.dto.PostDTO.ALPHACODE_09;
import static com.nixmash.springdata.jpa.utils.PostTestUtils.*;
import static com.nixmash.springdata.jpa.utils.PostUtils.postDtoToPost;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ApplicationConfig.class)
@ActiveProfiles(DataConfigProfile.H2)
@Transactional
public class PostServiceTests {

    @Autowired
    private PostService postService;

    @Autowired
    LikeRepository likeRepository;

    @Test
    public void addPostDTO() throws DuplicatePostNameException {
        PostDTO postDTO = PostTestUtils.createPostDTO(1);
        Post post = postService.add(postDTO);
        assertNotNull(post);
    }

    @Test
    public void postContainsAuthorObject() throws Exception {
        Post post = postService.getPostById(1L);
        assertNotNull(post.author);
    }

    @Test
    public void unpublishedPost_ShouldNotBeReturned_InFindAll() throws DuplicatePostNameException {
        PostDTO postDTO = PostTestUtils.createPostDTO(2);
        postDTO.setIsPublished(false);
        Post post = postService.add(postDTO);
        long postId = post.getPostId();
        assertThat(postId, greaterThan(1L));

        List<Post> posts = postService.getAllPublishedPosts();
        Optional<Post> unpublishedPost;
        unpublishedPost = posts.stream().filter(p -> p.getPostId().equals(post.getPostId())).findFirst();
        assertFalse(unpublishedPost.isPresent());

        List<Post> allposts = postService.getAllPosts();
        unpublishedPost = allposts.stream().filter(p -> p.getPostId().equals(post.getPostId())).findFirst();
        assertTrue(unpublishedPost.isPresent());
    }

    @Test
    public void updatePostDTO() throws PostNotFoundException {
        Post post = postService.getPostById(1L);
        PostDTO postDTO = PostUtils.postToPostDTO(post);
        String newTitle = "New Title 897";
        postDTO.setPostTitle(newTitle);
        Post update = postService.update(postDTO);
        assertEquals(update.getPostTitle(), newTitle);

        // PostName does not change...yet
        assertEquals(update.getPostName(), PostUtils.createSlug(post.getPostName()));
    }

    @Test
    public void builderShouldReturn_Null_ForMalformedLink() {
        PostDTO postDTO = PostDTO.getBuilder(USER_ID,
                POST_TITLE, POST_NAME, "malformed.link", POST_CONTENT, POST_TYPE, DISPLAY_TYPE).build();
        assertEquals(postDTO.getPostSource(), null);
    }

    @Test
    public void builderShouldReturnDomainAsPostSourceFromLink() {
        PostDTO postDTO = PostDTO.getBuilder(USER_ID,
                POST_TITLE, POST_NAME, "http://wellformed.link", POST_CONTENT, POST_TYPE, DISPLAY_TYPE).build();
        assertEquals(postDTO.getPostSource(), "wellformed.link");
    }

    @Test
    public void builderShouldReturn_Null_ForNullLink() {
        PostDTO postDTO = PostDTO.getBuilder(USER_ID,
                POST_TITLE, POST_NAME, null, POST_CONTENT, POST_TYPE, DISPLAY_TYPE).build();
        assertEquals(postDTO.getPostSource(), null);
    }

    @Test
    public void postDtoToPostShouldRetainPostSource() {
        PostDTO postDTO = PostDTO.getBuilder(USER_ID,
                POST_TITLE, POST_NAME, "http://wellformed.link", POST_CONTENT, POST_TYPE, DISPLAY_TYPE).build();
        assertEquals(postDTO.getPostSource(), "wellformed.link");
        Post post = postDtoToPost(postDTO);
        assertEquals(post.getPostSource(), "wellformed.link");
    }

    @Test
    public void postDtoToPostShouldRetainPostSourceOf_NA_ForNullLink() {
        PostDTO postDTO = PostDTO.getBuilder(USER_ID,
                POST_TITLE, POST_NAME, null, POST_CONTENT, POST_TYPE, DISPLAY_TYPE).build();
        assertEquals(postDTO.getPostSource(), null);
        Post post = postDtoToPost(postDTO);
        assertEquals(post.getPostSource(), null);
    }

    @Test
    public void findAllWithPaging() {
        Slice<Post> posts = postService.getPosts(0, 3);
        assertEquals(posts.getSize(), 3);
        ZonedDateTime firstPostDate = posts.getContent().get(0).getPostDate();
        ZonedDateTime secondPostDate = posts.getContent().get(1).getPostDate();

        // firstPostDate is higher (more recent) than secondPostDate with [sort: postDate: DESC]
        assertTrue(firstPostDate.compareTo(secondPostDate) > 0);
    }

    @Test
    public void getAllPostsIsGreaterThanPagedTotal() {
        List<Post> posts = postService.getAllPosts();
        assertThat(posts.size(), Matchers.greaterThan(3));
        ZonedDateTime firstPostDate = posts.get(0).getPostDate();
        ZonedDateTime secondPostDate = posts.get(1).getPostDate();

        // firstPostDate is higher (more recent) than secondPostDate with [sort: postDate: DESC]
        assertTrue(firstPostDate.compareTo(secondPostDate) > 0);
    }

    @Test
    public void findPostsByTagId() {
        Slice<Post> posts = postService.getPostsByTagId(1, 0, 3);

        // posts are retrieved for tagId #1 as all 5 H2 posts have tagId #1
        assertEquals(posts.getSize(), 3);
    }

    @Test
    public void findAllWithDetails() {
        List<Post> posts = postService.getPostsWithDetail();
        assertNotNull(posts);
    }

    @Test
    public void addPostWithTags() throws DuplicatePostNameException, PostNotFoundException {
        PostDTO postDTO = PostTestUtils.createPostDTO(3);
        postDTO.getTags().add(new TagDTO("addPostWithTags1"));
        postDTO.getTags().add(new TagDTO("addPostWithTags2"));
        Post post = postService.add(postDTO);
        assertEquals(post.getTags().size(), 2);

        Post retrieved = postService.getPostById(post.getPostId());
        assertEquals(retrieved.getTags().size(), 2);
    }

    @Test
    public void updatePostWithTags() throws DuplicatePostNameException, PostNotFoundException {

        // Post(5L) is loaded with 2 tags in H2
        Post post = postService.getPostById(5L);
        PostDTO postDTO = PostUtils.postToPostDTO(post);
        postDTO.getTags().add(new TagDTO("updatePostWithTags1"));
        Post updated = postService.update(postDTO);
        assertEquals(updated.getTags().size(), 3);

        Post retrieved = postService.getPostById(5L);
        assertEquals(retrieved.getTags().size(), 3);
    }

    @Test
    public void getTagCloud_TagListNotNull() throws Exception {
        List<TagDTO> tagcloud = postService.getTagCloud(50);
        assertThat(tagcloud.get(0).getTagCount(), greaterThan(0));
        assertNotNull(tagcloud);
    }

    @Test
    public void getPostsByUserLikes() {
        // userId 3 "keith" has 3 likes, userId 2 "user" has 2 likes
        List<Post> posts = postService.getPostsByUserLikes(3L);
        assertNotNull(posts);
    }

    @Test
    public void getLikedPostsForUserWithNoLikes_NotNull() {
        // no likes for userId = 6 in H2 data
        List<Post> posts = postService.getPostsByUserLikes(6L);
        assertNull(posts);
    }

    @Test
    public void getPostsByPostType() throws Exception{
        List<Post> posts;
        posts = postService.getAllPublishedPostsByPostType(PostType.POST);
        assertNotNull(posts);
        posts = postService.getAllPublishedPostsByPostType(PostType.LINK);
        assertNotNull(posts);
    }

    @Test
    public void getPagedPostsByPostType() throws Exception{
        Page<Post> posts;
        posts = postService.getPagedPostsByPostType(PostType.POST, 0, 10);
        assertNotNull(posts);
        posts = postService.getPagedPostsByPostType(PostType.LINK, 0, 10);
        assertNotNull(posts);
    }

    @Test
    public void addLikedPost_UserWithNoLikes_ReturnsPlusOne()
            throws PostNotFoundException {

        //  H2DATA:  no likes for any posts for userId 4 ------------------------------------ */

        // get initial LikeCount for postId 3
        int likeCount = postService.getPostById(3L).getLikesCount();

        // new Like for postId from userId 4. Should return increment value 1
        int increment = postService.addPostLike(4L, 3L);
        assertEquals(increment, 1);

        // LikeCount for postId 3 should increment by 1
        int updatedLikeCount = postService.getPostById(3L).getLikesCount();
        assertEquals(updatedLikeCount, likeCount + 1);

        // confirm like added to user_likes table
        Optional<Long> likeId = likeRepository.findPostLikeIdByUserId(4L, 3L);
        assert (likeId.isPresent());
    }

    @Test
    public void addLikedPost_UserPreviouslyLikedPost_ReturnsMinusOne()
            throws PostNotFoundException {

        //  H2DATA: userId  3 has pre-existing Like for postId 10------------------------------------ */

        // initial Like count for postId 3
        int likeCount = postService.getPostById(10L).getLikesCount();

        // addPostLike(userId, postId) should return -1 which removes existing Post Like
        int increment = postService.addPostLike(3L, 10L);
        assertEquals(increment, -1);

        // postId 3 should have one less LikeCount
        int updatedLikeCount = postService.getPostById(10L).getLikesCount();
        assertEquals(updatedLikeCount, likeCount - 1);

        // pre-existing like removes record from user_likes table: should NOT be present
        Optional<Long> likeId = likeRepository.findPostLikeIdByUserId(3L, 10L);
        assert (!likeId.isPresent());
    }

    @Test
    public void pagedLikedPostsTest() {
        List<Post> posts = postService.getPagedLikedPosts(3, 0, 2);
        // list contains 2 posts
        assertEquals(posts.size(), 2);

        ZonedDateTime firstPostDate = posts.get(0).getPostDate();
        ZonedDateTime secondPostDate = posts.get(1).getPostDate();

        // first PostDate is higher (more recent) than second PostDate [sort: postDate: DESC]
        assertTrue(firstPostDate.compareTo(secondPostDate) > 0);

    }

    @Test
    public void alphaLinksContainsActive() {
        // AlphabetDTO characters isActive() property set to true by first letter in Post Titles
        List<AlphabetDTO> alphabetDTOs = postService.getAlphaLInks();
        assertThat(alphabetDTOs, hasItem(Matchers.<AlphabetDTO>hasProperty("active", equalTo(true))));
    }

    @Test
    public void alphaLinksContainsActive0to9() {
        // AlphabetDTO has an alphaCharacter  of "0-9" and is TRUE from H2 Test Post Titles
        List<AlphabetDTO> alphabetDTOs = postService.getAlphaLInks();
        Optional<AlphabetDTO> alphabetDTO = alphabetDTOs
                .stream()
                .filter(a -> (a.getAlphaCharacter().equals("0-9") && a.getActive().equals(true)))
                .findFirst();
        assert (alphabetDTO.isPresent());
    }

    @Test
    public void confirm0to9alphaLinkisFirstRecord() {
        // Confirm that the first item in the list is "0-9"
        assertEquals(postService.getAlphaLInks().get(0).getAlphaCharacter(), "0-9");
    }

    @Test
    public void alphaPostsStartingWithDigitsHave_09_inAphaKeyField() {
        List<PostDTO> posts = postService.getAlphaPosts();

        // There are 3 post titles in H2 beginning with digits:  1 - two, 2 - one
        for (int i = 1; i < 3; i++) {
            int finalI = i;
            Optional<PostDTO> postDTO = posts
                    .stream()
                    .filter(p -> (p.getPostTitle().substring(0, 1).equals(String.valueOf(finalI))))
                    .findFirst();
            assert (postDTO.isPresent());
            assertEquals(postDTO.get().getAlphaKey(), ALPHACODE_09);
        }
    }

    @Test
    public void alphaPostsHaveAlphaKeyOfStartingLetter() {
        List<PostDTO> posts = postService.getAlphaPosts();
        Optional<PostDTO> postDTO = posts
                .stream()
                .filter(p -> (p.getPostTitle().substring(0, 1).equals("A")))
                .findFirst();
        assert (postDTO.isPresent());
        assertEquals(postDTO.get().getAlphaKey(), "A");

    }

    @Test
    public void postImagesLoad() {
        List<PostImage> postImages = postService.getPostImages(1L);
        assertEquals(postImages.size(), 2);
    }

    @Test
    public void allPostImagesLoad() {
        List<PostImage> postImages = postService.getAllPostImages();
        assertEquals(postImages.size(), 3);
    }

    @Test
    public void negativePostIdStub_NotYetSelected() throws PostNotFoundException {
        Post post = postService.getPostById(-1L);
        assertEquals(post.getPostName(), "not-yet-selected");

    }
}
