package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.dto.AlphabetDTO;
import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.dto.TagDTO;
import com.nixmash.springdata.jpa.enums.PostType;
import com.nixmash.springdata.jpa.exceptions.DuplicatePostNameException;
import com.nixmash.springdata.jpa.exceptions.PostNotFoundException;
import com.nixmash.springdata.jpa.exceptions.TagNotFoundException;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.model.PostImage;
import com.nixmash.springdata.jpa.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by daveburke on 6/1/16.
 */
public interface PostService {

    Post add(PostDTO postDTO) throws DuplicatePostNameException;

    Post getPost(String postName) throws PostNotFoundException;

    Page<Post> getPosts(Integer pageNumber, Integer pageSize);

    @Transactional
    Post update(PostDTO postDTO) throws PostNotFoundException;

    @Transactional(readOnly = true)
    List<Post> getPostsByUserLikes(Long userId);

    @Transactional(readOnly = true)
    List<Post> getPagedLikedPosts(long userId, int pageNumber, int pageSize);

    @Transactional
    int addPostLike(long userId, long postId);

    Post getPostById(Long postId) throws PostNotFoundException;

    @Transactional(readOnly = true)
    Page<Post> getPublishedPosts(Integer pageNumber, Integer pageSize);

    @Transactional(readOnly = true)
    List<Post> getAllPosts();

    @Transactional(readOnly = true)
    List<Post> getAllPublishedPostsByPostType(PostType postType);

    @Transactional(readOnly = true)
    Page<Post> getPagedPostsByPostType(PostType postType, int pageNumber, int pageSize);

    @Transactional(readOnly = true)
    List<Post> getAllPublishedPosts();

    Optional<Post> getOneMostRecent();

    @Transactional(readOnly = true)
    List<Post> getPostsWithDetail();

    @Transactional
    Tag createTag(TagDTO tagDTO);

    @Transactional
    Tag updateTag(TagDTO tagDTO);

    @Transactional
    void deleteTag(TagDTO tagDTO, List<Post> posts);

    @Transactional(readOnly = true)
    Set<TagDTO> getTagDTOs();

    @Transactional(readOnly = true)
    List<TagDTO> getTagCloud(int tagCount);

    @Transactional(readOnly = true)
    List<AlphabetDTO> getAlphaLInks();

    @Transactional(readOnly = true)
    List<PostDTO> getAlphaPosts();

    boolean canUpdatePost(Authentication authentication, Long postId);

    @Transactional(readOnly = true)
    List<String> getTagValues();

    Set<TagDTO> getTagDTOs(Long postId);

    @Transactional(readOnly = true)
    List<PostImage> getAllPostImages();

    @Transactional(readOnly = true)
    List<PostImage> getPostImages(long postId);

    @Transactional
    PostImage addImage(PostImage image);

    @Transactional(readOnly = true)
    PostImage getPostImage(long imageId);

    Tag getTag(String tagValue) throws TagNotFoundException;

    Page<Post> getPostsByTagId(long tagId, int pageNumber, int pageSize);

    List<Post> getPostsByTagId(long tagId);

    void deleteImage(PostImage image);
}
