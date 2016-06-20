package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.exceptions.DuplicatePostNameException;
import com.nixmash.springdata.jpa.exceptions.PostNotFoundException;
import com.nixmash.springdata.jpa.model.CurrentUser;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.repository.PostRepository;
import com.nixmash.springdata.jpa.utils.PostUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by daveburke on 6/1/16.
 */
@Service
public class PostServiceImpl implements PostService{

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    //region Add / UpdatePost

    @Override
    public Post add(PostDTO postDTO) throws DuplicatePostNameException {
        Post post;
        try {
            post =  postRepository.save(PostUtils.postDtoToPost(postDTO));
        } catch (Exception e) {
            throw new DuplicatePostNameException("Duplicate Post Name for Post Title: " +
                    postDTO.getPostTitle());
        }

        return post;
    }

    @Transactional(rollbackFor = PostNotFoundException.class)
    @Override
    public Post update(PostDTO postDTO) throws PostNotFoundException {

        Post post = postRepository.findByPostId(postDTO.getPostId());
        post.update(postDTO.getPostTitle(), postDTO.getPostContent());
        return post;
    }

        //endregion


    //region Get Posts

    @Transactional(readOnly = true)
    @Override
    public Post getPostById(Long ID) throws PostNotFoundException {
        Post found = postRepository.findByPostId(ID);
        if (found == null) {
            logger.info("No post found with id: {}", ID);
            throw new PostNotFoundException("No post found with id: " + ID);
        }
        return found;
    }

    @Transactional(readOnly = true)
    @Override
    public Post getPost(String postName) throws PostNotFoundException {
        Post found =  postRepository.findByPostNameIgnoreCase(postName);
        if (found == null) {
            logger.info("No post found with id: {}", postName);
            throw new PostNotFoundException("No post found with id: " + postName);
        }

        return found;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Post> getPosts(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest =
                new PageRequest(pageNumber, pageSize, sortByPostDateDesc());
        return postRepository.findAll(pageRequest);
    }

    //endregion


    // region Security Support


    @Override
    public boolean canUpdatePost(Authentication authentication, Long postId) {

        if (authentication instanceof AnonymousAuthenticationToken)
            return false;

        CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();

        Post post = null;
        try {
            post = getPostById(postId);
        } catch (PostNotFoundException e) {
            logger.error("Post not found for PostId {} ", postId);
            return false;
        }

        Long postUserId = post.getUserId();
        logger.info("Checking if user={} can update post '{}'", currentUser.getUsername(), post.getPostTitle());
        return currentUser.getId().equals(postUserId);
    }


    // endregion

    //region Utility methods

    public Sort sortByPostDateDesc() {
        return new Sort(Sort.Direction.DESC, "postDate");
    }

    //endregion
}
