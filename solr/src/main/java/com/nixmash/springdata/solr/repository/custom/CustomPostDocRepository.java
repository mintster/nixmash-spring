/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nixmash.springdata.solr.repository.custom;

import com.nixmash.springdata.solr.model.PostDoc;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

public interface CustomPostDocRepository extends CustomBasePostDocRepository, SolrCrudRepository<PostDoc, String> {

    @Query("doctype:post")
    List<PostDoc> findAllPostDocuments();

    @Query("doctype:post AND id:?0")
    PostDoc findPostDocByPostId(long postId);
}
