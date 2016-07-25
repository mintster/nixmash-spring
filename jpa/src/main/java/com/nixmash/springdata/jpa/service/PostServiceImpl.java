package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.dto.TagDTO;
import com.nixmash.springdata.jpa.enums.ContentType;
import com.nixmash.springdata.jpa.exceptions.DuplicatePostNameException;
import com.nixmash.springdata.jpa.exceptions.PostNotFoundException;
import com.nixmash.springdata.jpa.exceptions.TagNotFoundException;
import com.nixmash.springdata.jpa.model.CurrentUser;
import com.nixmash.springdata.jpa.model.Like;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.model.Tag;
import com.nixmash.springdata.jpa.repository.LikeRepository;
import com.nixmash.springdata.jpa.repository.PostRepository;
import com.nixmash.springdata.jpa.repository.TagRepository;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by daveburke on 6/1/16.
 */
@Service("postService")
@Transactional
public class PostServiceImpl implements PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    private PostRepository postRepository;
    private TagRepository tagRepository;
    private LikeRepository likeRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, TagRepository tagRepository, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.likeRepository = likeRepository;
    }

    @PersistenceContext
    private EntityManager em;

    //region Add / UpdatePost

    @Transactional(rollbackFor = DuplicatePostNameException.class)
    @Override
    public Post add(PostDTO postDTO) throws DuplicatePostNameException {
        Post post;
        try {
            post = postRepository.save(PostUtils.postDtoToPost(postDTO));

        } catch (Exception e) {
            throw new DuplicatePostNameException("Duplicate Post Name for Post Title: " +
                    postDTO.getPostTitle());
        }

        if (postDTO.getTags() != null) {

            saveNewTagsToDataBase(postDTO);

            post.setTags(new HashSet<>());
            for (TagDTO tagDTO : postDTO.getTags()) {
                Tag tag = tagRepository.findByTagValueIgnoreCase(tagDTO.getTagValue());
                post.getTags().add(tag);
            }
        }

        return post;
    }

    @Transactional(rollbackFor = PostNotFoundException.class)
    @Override
    public Post update(PostDTO postDTO) throws PostNotFoundException {

        Post post = postRepository.findByPostId(postDTO.getPostId());
        post.update(postDTO.getPostTitle(), postDTO.getPostContent());

        saveNewTagsToDataBase(postDTO);

        post.getTags().clear();
        for (TagDTO tagDTO : postDTO.getTags()) {
            Tag tag = tagRepository.findByTagValueIgnoreCase(tagDTO.getTagValue());

            if (!post.getTags().contains(tag))
                post.getTags().add(tag);
        }

        return post;
    }

    //endregion

    // region Likes

    @Transactional(readOnly = true)
    @Override
    public List<Post> getPostsByUserLikes(Long userId) {
        List<Post> posts = em.createNamedQuery("Post.getByPostIds", Post.class)
                .setParameter("postIds", likeRepository.findLikedPostIds(userId))
                .getResultList();
        return posts;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Post> getPagedLikedPosts(long userId, int pageNumber, int pageSize) {
        List<Post> posts = em.createNamedQuery("Post.getByPostIds", Post.class)
                .setParameter("postIds", likeRepository.findLikedPostIds(userId))
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        return posts;
    }

    @Transactional
    @Override
    public int addPostLike(long userId, long postId) {
        int incrementValue = 1;
        Post post = postRepository.findByPostId(postId);
        Optional<Long> likeId = likeRepository.findPostLikeIdByUserId(userId, postId);
        if (likeId.isPresent()) {
            incrementValue = -1;
            likeRepository.delete(likeId.get());
        } else {
            Like like = new Like(userId, postId, ContentType.POST);
            likeRepository.save(like);
        }
        post.updateLikes(incrementValue);
        return incrementValue;
    }

    // endregion

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
        Post found = postRepository.findByPostNameIgnoreCase(postName);
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

    @Transactional(readOnly = true)
    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll(sortByPostDateDesc());
    }

    @Override
    public Optional<Post> getOneMostRecent() {
        logger.debug("Getting most recent post");
        Page<Post> posts = postRepository.findAll(new PageRequest(0, 1, sortByPostDateDesc()));
        if (posts.getContent().isEmpty()) {
            logger.debug("No documents");
            return Optional.empty();
        } else {
            Post post = posts.getContent().get(0);
            logger.trace("Returning {}", post);
            return Optional.of(post);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Post> getPostsWithDetail() {
        return postRepository.findAllWithDetail();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Post> getPostsByTagId(long tagId, int pageNumber, int pageSize) {
        PageRequest pageRequest =
                new PageRequest(pageNumber, pageSize, sortByPostDateDesc());
        return postRepository.findByTagId(tagId, pageRequest);
    }

    @Override
    public List<Post> getPostsByTagId(long tagId) {
        return postRepository.findAllByTagId(tagId);
    }

    //endregion

    // region Tags

    @Transactional(readOnly = true)
    @Override
    public Tag getTag(String tagValue) throws TagNotFoundException {
        Tag found = tagRepository.findByTagValueIgnoreCase(tagValue);
        if (found == null) {
            logger.info("No tag found with id: {}", tagValue);
            throw new TagNotFoundException("No tag found with id: " + tagValue);
        }

        return found;
    }

    @Transactional
    private void saveNewTagsToDataBase(PostDTO postDTO) {
        for (TagDTO tagDTO : postDTO.getTags()) {
            Tag tag = tagRepository.findByTagValueIgnoreCase(tagDTO.getTagValue());
            if (tag == null) {
                tag = new Tag(tagDTO.getTagValue());
                tagRepository.save(tag);
            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Set<TagDTO> getTagDTOs() {
        Set<Tag> tags = tagRepository.findAll();
        return PostUtils.tagsToTagDTOs(tags);
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> getTagValues() {
        Set<Tag> tags = tagRepository.findAll();
        return PostUtils.tagsToTagValues(tags);
    }


    @Transactional(readOnly = true)
    @Override
    public Set<TagDTO> getTagDTOs(Long postId) {
        return null;
    }


    @Transactional(readOnly = true)
    @Override
    public List<TagDTO> getTagCloud() {
        List<Tag> tagcloud = em.createNamedQuery("getTagCloud", Tag.class)
                .getResultList();
        List<TagDTO> tagDTOs = tagcloud
                .stream()
                .filter(t -> t.getPosts().size() > 0)
                .limit(50)
                .map(TagDTO::new)
                .collect(Collectors.toList());
        return tagDTOs;
    }

    // endregion

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
        return currentUser.getId().equals(postUserId);
    }

    // endregion

    //region Utility methods

    public Sort sortByPostDateDesc() {
        return new Sort(Sort.Direction.DESC, "postDate");
    }

    //endregion
}
