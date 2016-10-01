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
package com.nixmash.springdata.solr.model;

import com.nixmash.springdata.jpa.model.Tag;
import com.nixmash.springdata.solr.enums.SolrDocType;
import org.apache.solr.client.solrj.beans.Field;
import org.jsoup.Jsoup;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class PostDoc implements IPostDoc {

    // region Properties

    @Field(ID)
    private String postId;

    @Field(POST_TITLE)
    private String postTitle;

    @Field(POST_AUTHOR)
    private String postAuthor;

    @Field(POST_NAME)
    private String postName;

    @Field(POST_LINK)
    private String postLink;

    @Field(POST_DATE)
    private Date postDate;

    @Field(POST_TYPE)
    private String postType;

    @Field(HTML)
    private String postHTML;

    @Field(POST_TEXT)
    private String postText;

    @Field(POST_SOURCE)
    private String postSource;

    @Field(TAG)
    private List<String> tags;

    @Field(DOCTYPE)
    private String docType;


    // endregion

    // region Constructors

    public PostDoc() {
    }

    ;

    // endregion

    // region Getter Setters

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostAuthor() {
        return postAuthor;
    }

    public void setPostAuthor(String postAuthor) {
        this.postAuthor = postAuthor;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostLink() {
        return postLink;
    }

    public void setPostLink(String postLink) {
        this.postLink = postLink;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostHTML() {
        return postHTML;
    }

    public void setPostHTML(String postHTML) {
        this.postHTML = postHTML;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostSource() {
        return postSource;
    }

    public void setPostSource(String postSource) {
        this.postSource = postSource;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }


    // endregion

    // region toString

    @Override
    public String toString() {
        return "PostDoc{" +
                "postId='" + postId + '\'' +
                ", postTitle='" + postTitle + '\'' +
                ", postAuthor='" + postAuthor + '\'' +
                ", postName='" + postName + '\'' +
                ", postLink='" + postLink + '\'' +
                ", postDate=" + postDate +
                ", postType='" + postType + '\'' +
                ", postHTML='" + postHTML + '\'' +
                ", postText='" + postText + '\'' +
                ", postSource='" + postSource + '\'' +
                ", tags=" + tags +
                ", docType='" + docType + '\'' +
                '}';
    }

    // endregion

    // region Builders

    public static Builder getBuilder(Long postId, String postTitle, String postAuthor,
                                     String postName, String postLink, String postHTML,
                                     String postSource, String postType) {
        return new PostDoc.Builder(postId, postTitle, postAuthor, postName,
                postLink, postHTML, postSource, postType);
    }


    public static class Builder {
        private PostDoc built;

        public Builder(Long postId, String postTitle, String postAuthor, String postName,
                       String postLink, String postHTML, String postSource, String postType) {
            built = new PostDoc();
            built.postId = postId.toString();
            built.postTitle = postTitle;
            built.postAuthor = postAuthor;
            built.postName = postName;
            built.postLink = postLink;
            built.postHTML = postHTML;
            built.postText = Jsoup.parse(postHTML).text();
            built.postSource = postSource;
            built.postType = postType;
            built.docType = SolrDocType.POST;
        }

        public Builder tags(Set<Tag> tags) {
            List<String> tagsList = tags
                    .stream()
                    .map(Tag::getTagValue)
                    .collect(Collectors.toList());
            built.tags = tagsList;
            return this;
        }

        public Builder postDate(ZonedDateTime postDate) {
            built.postDate = Date.from(postDate.toInstant());
            return this;
        }


        public PostDoc build() {
            return built;
        }
    }


    // endregion


}
