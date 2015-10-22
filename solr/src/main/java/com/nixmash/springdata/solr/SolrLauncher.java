package com.nixmash.springdata.solr;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.SpringVersion;

import com.nixmash.springdata.solr.config.SolrApplicationConfig;

public class SolrLauncher {

    public static void main(String[] args) {
    	   AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
           ctx.register(SolrApplicationConfig.class);
           ctx.refresh();
           System.out.println("version: " + SpringVersion.getVersion());
    }
}
