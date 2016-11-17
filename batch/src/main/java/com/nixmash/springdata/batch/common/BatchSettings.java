package com.nixmash.springdata.batch.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("file:/home/daveburke/web/nixmashspring/jobs.properties")
@ConfigurationProperties(prefix="jobs")
public class BatchSettings {

    private Integer importPostJobParam1;
    private String importPostJobParam2;

    public Integer getImportPostJobParam1() {
        return importPostJobParam1;
    }

    public void setImportPostJobParam1(Integer importPostJobParam1) {
        this.importPostJobParam1 = importPostJobParam1;
    }

    public String getImportPostJobParam2() {
        return importPostJobParam2;
    }

    public void setImportPostJobParam2(String importPostJobParam2) {
        this.importPostJobParam2 = importPostJobParam2;
    }
}
