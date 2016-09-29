package com.nixmash.springdata.solr.service;

import com.nixmash.springdata.solr.model.PostDoc;

import java.util.List;

/**
 * Created by daveburke on 9/29/16.
 */
public interface PostService {

    List<PostDoc> getPostsWithUserQuery(String userQuery);
}
