package com.nixmash.springdata.mail.service;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

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

}
