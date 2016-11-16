package com.nixmash.springdata.batch.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("file:/home/daveburke/web/nixmashspring/batch.properties")
@ConfigurationProperties(prefix="jobs")
public class BatchSettings {

    public Boolean getEnableImportJob() {
        return enableImportJob;
    }

    public void setEnableImportJob(Boolean enableImportJob) {
        this.enableImportJob = enableImportJob;
    }

    public Boolean enableImportJob;

}
