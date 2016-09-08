package com.nixmash.springdata.mail.service;

import com.nixmash.springdata.jpa.common.ApplicationSettings;
import com.nixmash.springdata.jpa.model.Post;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.EscapeTool;
import org.apache.velocity.tools.generic.MathTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.*;

import java.time.format.DateTimeFormatter;
import java.util.Hashtable;
import java.util.Map;

@Service("templateService")
@SuppressWarnings("deprecation")
public class TemplateServiceImpl implements TemplateService {

    private static final Logger logger = LoggerFactory.getLogger(TemplateServiceImpl.class);
    final private VelocityEngine velocityEngine;

    @Autowired
    Environment environment;

    @Autowired
    ApplicationSettings applicationSettings;

    @Autowired
    public TemplateServiceImpl(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    @Override
    public String getRobotsTxt() {
        return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "robots.vm", "UTF-8", null);
    }

    @Override
    public String getFileUploadingScript() {
        return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "posts/fileuploading.vm", "UTF-8", null);
    }

    @Override
    public String getFileUploadedScript() {
        return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "posts/fileuploaded.vm", "UTF-8", null);
    }


    @Override
    public String createPostHtml(Post post, String templateName) {
        String html = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        String postCreated = post.getPostDate().format(formatter);

        Map<String, Object> model = modelWithTools();

        model.put("post", post);
        model.put("postCreated", postCreated);
        model.put("shareSiteName",
                StringUtils.deleteWhitespace(applicationSettings.getSiteName()));
        model.put("shareUrl",
                String.format("%s/posts/post/%s", applicationSettings.getBaseUrl(), post.getPostName()));

        try {
            String displayType = templateName == null ? post.getDisplayType().name().toLowerCase() : templateName;
            String template = String.format("posts/%s.vm", displayType);

            html = VelocityEngineUtils
                    .mergeTemplateIntoString(velocityEngine, template, "UTF-8", model);
        } catch (Exception e) {
            logger.error("Problem merging post template : " + e.getMessage());
        }
        return html;
    }

    @Override
    public String createPostHtml(Post post) {
        return createPostHtml(post, null);
    }

    @Override
    public String getNoLikesMessage() {

        Map<String, Object> model = modelWithTools();

        return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "posts/nolikes.vm", "UTF-8", null);
    }

    private Map<String, Object> modelWithTools() {
        Map<String, Object> model = new Hashtable<>();
        model.put("esc", new EscapeTool());
        model.put("math", new MathTool());
        return model;
    }

}
