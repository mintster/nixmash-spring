package com.nixmash.springdata.solr.service;

import com.nixmash.springdata.solr.model.PostDoc;
import com.nixmash.springdata.solr.repository.custom.CustomPostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Resource
    CustomPostRepository postRepository;

    @Override
    public List<PostDoc> getPostsWithUserQuery(String userQuery) {
        logger.info("SimpleQuery from user search string -  findProductsBySimpleQuery()");
        return postRepository.findPostsBySimpleQuery(userQuery);
    }

}
