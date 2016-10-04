package com.nixmash.springdata.mail.service;

import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.model.User;

public interface FmService {

    String displayTestTemplate(User user);

    String getNoResultsMessage(String search);

    String getNoLikesMessage();

    String createPostHtml(Post post, String templateName);

    String createPostHtml(Post post);

    String getRobotsTxt();

    String getFileUploadingScript();

    String getFileUploadedScript();
}
