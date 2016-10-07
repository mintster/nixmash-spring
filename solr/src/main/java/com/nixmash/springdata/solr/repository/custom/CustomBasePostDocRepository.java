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

import com.nixmash.springdata.jpa.dto.PostQueryDTO;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.solr.model.PostDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.UncategorizedSolrException;

import java.util.List;

public interface CustomBasePostDocRepository {

    List<PostDoc> findPostsBySimpleQuery(String userQuery) throws UncategorizedSolrException;

    void update(Post post);

    Page<PostDoc> pagedQuickSearch(String searchTerms, PageRequest pageRequest);

    Page<PostDoc> pagedFullSearch(PostQueryDTO postQueryDTO, PageRequest pageRequest);

    List<PostDoc> fullSearch(PostQueryDTO postQueryDTO) throws UncategorizedSolrException;

    List<PostDoc> quickSearch(String searchTerm);

}
