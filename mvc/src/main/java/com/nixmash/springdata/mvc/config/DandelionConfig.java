/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nixmash.springdata.mvc.config;

import com.github.dandelion.core.web.DandelionFilter;
import com.github.dandelion.core.web.DandelionServlet;
import com.github.dandelion.thymeleaf.dialect.DandelionDialect;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Configuration class to set up Dandelion-related beans.
 *
 * @author Arnaldo Piccinelli
 */
@Component
@Configuration
public class DandelionConfig {

    @Bean
    public DandelionDialect dandelionDialect() {
        return new DandelionDialect();
    }

    @Bean
    public FilterRegistrationBean dandelionFilterRegistrationBean() {
        return new FilterRegistrationBean(new DandelionFilter());
    }


    @Bean
    public ServletRegistrationBean dandelionServletRegistrationBean() {
        return new ServletRegistrationBean(new DandelionServlet(),
                "/dandelion-assets/*");
    }
}



