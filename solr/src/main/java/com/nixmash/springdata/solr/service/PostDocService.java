package com.nixmash.springdata.solr.service;

import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.solr.model.PostDoc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by daveburke on 9/29/16.
 */
public interface PostDocService {

    List<PostDoc> getPostsWithUserQuery(String userQuery);

    @Transactional
    void addToIndex(Post post);

    @Transactional
    void updatePostDocument(Post post);

    List<PostDoc> getAllPostDocuments();
}
