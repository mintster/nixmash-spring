package com.nixmash.springdata.solr.service;

import com.nixmash.springdata.jpa.dto.PostQueryDTO;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.solr.model.PostDoc;
import com.nixmash.springdata.solr.repository.custom.CustomPostDocRepository;
import com.nixmash.springdata.solr.utils.SolrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostDocServiceImpl implements PostDocService {

    private static final Logger logger = LoggerFactory.getLogger(PostDocServiceImpl.class);

    @Resource
    CustomPostDocRepository customPostDocRepository;

    private final SolrOperations solrOperations;

    @Autowired
    public PostDocServiceImpl(SolrOperations solrOperations) {
        this.solrOperations = solrOperations;
    }

    @Override
    public List<PostDoc> getPostsWithUserQuery(String userQuery) {
        logger.info("SimpleQuery from user search string -  findProductsBySimpleQuery()");
        return customPostDocRepository.findPostsBySimpleQuery(userQuery);
    }

    @Transactional
    @Override
    public void addToIndex(Post post) {
        logger.info("Saving a Post Document with information: {}", post);
        PostDoc document = SolrUtils.createPostDoc(post);
        customPostDocRepository.save(document);
        commit();
    }

    @Transactional
    @Override
    public void reindexPosts(List<Post> posts) {
        Query query = new SimpleQuery(new SimpleStringCriteria("doctype:post"));
        solrOperations.delete(query);
        solrOperations.commit();
        addAllToIndex(posts);
    }

    @Transactional
    @Override
    public void addAllToIndex(List<Post> posts) {
        logger.info("Saving all Post Documents to Index");
        List<PostDoc> postDocs = new ArrayList<>();
        for (Post post : posts
                ) {
            postDocs.add(SolrUtils.createPostDoc(post));
        }
        customPostDocRepository.save(postDocs);
        commit();
    }

    @Transactional
    @Override
    public void removeFromIndex(PostDoc postDoc) {
        customPostDocRepository.delete(postDoc);
        commit();
    }

    @Transactional
    @Override
    public void removeFromIndex(List<PostDoc> postDocs) {
        customPostDocRepository.delete(postDocs);
        commit();
    }

    @Transactional
    @Override
    public void removeFromIndex(String query) {
        solrOperations.delete(new SimpleQuery(query));
        commit();
    }

    private void commit() {
        solrOperations.getSolrClient();
        solrOperations.commit();
    }

    @Override
    public void updatePostDocument(Post post) {
        customPostDocRepository.update(post);
    }

    @Override
    public PostDoc getPostDocByPostId(long postId) {
        return customPostDocRepository.findPostDocByPostId(postId);
    }

    @Override
    public List<PostDoc> getAllPostDocuments() {
       return customPostDocRepository.findAllPostDocuments();
    }

    @Override
    public List<PostDoc> doQuickSearch(String searchTerm) {
        return customPostDocRepository.quickSearch(searchTerm);
    }

    @Override
    public List<PostDoc> doFullSearch(PostQueryDTO postQueryDTO) {
        return customPostDocRepository.fullSearch(postQueryDTO);
    }

    @Override
    public Page<PostDoc> doPagedFullSearch(PostQueryDTO postQueryDTO, int pageNumber, int pageSize) {
        PageRequest pageRequest =
                new PageRequest(pageNumber, pageSize, sortByPostDateDesc());
        return customPostDocRepository.pagedFullSearch(postQueryDTO, pageRequest);
    }

    @Override
    public Page<PostDoc> doPagedQuickSearch(String searchTerms, int pageNumber, int pageSize) {
        PageRequest pageRequest =
                new PageRequest(pageNumber, pageSize, sortByPostDateDesc());
        return customPostDocRepository.pagedQuickSearch(searchTerms, pageRequest);
    }

    public Sort sortByPostDateDesc() {
        return new Sort(Sort.Direction.DESC, PostDoc.POST_DATE);
    }
}
