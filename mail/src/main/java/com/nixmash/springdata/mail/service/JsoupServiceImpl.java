package com.nixmash.springdata.mail.service;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Service("jsoupService")
public class JsoupServiceImpl implements JsoupService {

    final private VelocityEngine velocityEngine;

    @Autowired
    public JsoupServiceImpl(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    @Override
    public String getDemoJsoupHTML() {
        return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "jsoup.vm", "UTF-8", null);
    }
}
