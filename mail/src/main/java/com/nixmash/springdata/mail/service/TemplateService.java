package com.nixmash.springdata.mail.service;

import com.nixmash.springdata.jpa.model.Post;

public interface TemplateService {

    String getRobotsTxt();

    String getFileUploadingScript();

    String getFileUploadedScript();

    String createPostHtml(Post post, String template);

    String createPostHtml(Post post);

    String getNoLikesMessage();
}
