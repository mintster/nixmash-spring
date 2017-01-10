package com.nixmash.springdata.mail.service;

import com.nixmash.springdata.jpa.dto.AlphabetDTO;
import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.model.User;

import java.util.List;

public interface FmService {

    String displayTestTemplate(User user);

    String getNoResultsMessage(String search);

    String getNoLikesMessage();

    String createPostHtml(Post post, String templateName);

    String createPostHtml(Post post);

    String createPostAtoZs(List<AlphabetDTO> alphaLinks, List<PostDTO> alphaPosts);

    String getRobotsTxt();

    String getFileUploadingScript();

    String getFileUploadedScript();
}
