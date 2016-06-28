package com.nixmash.springdata.mail.service;

import com.nixmash.springdata.jpa.model.Post;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.EscapeTool;
import org.apache.velocity.tools.generic.MathTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.time.format.DateTimeFormatter;
import java.util.Hashtable;
import java.util.Map;

@Service("templateService")
public class TemplateServiceImpl implements TemplateService {

    private static final Logger logger = LoggerFactory.getLogger(TemplateServiceImpl.class);
    final private VelocityEngine velocityEngine;

    @Autowired
    Environment environment;

    @Autowired
    public TemplateServiceImpl(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    @Override
    public String getRobotsTxt() {
        return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "robots.vm", "UTF-8", null);
    }

    @Override
    public String createPostHtml(Post post) {
        String html = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        String postCreated = post.getPostDate().format(formatter);

        Map<String, Object> model = modelWithTools();

        model.put("post", post);
        model.put("postCreated", postCreated);

        try {
            String displayType = post.getDisplayType().name().toLowerCase();
            String template = String.format("posts/%s.vm", displayType);

            html = VelocityEngineUtils
                    .mergeTemplateIntoString(velocityEngine, template, "UTF-8", model);
        } catch (Exception e) {
            logger.error("Problem merging post template : " + e.getMessage());
        }
        return html;
    }

    private Map<String, Object> modelWithTools() {
        Map<String, Object> model = new Hashtable<>();
        model.put("esc", new EscapeTool());
        model.put("math", new MathTool());
        return model;
    }

}
