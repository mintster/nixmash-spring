package com.nixmash.springdata.jpa.config;

import com.nixmash.springdata.jpa.model.auditors.CurrentTimeDateTimeService;
import com.nixmash.springdata.jpa.model.auditors.DateTimeService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created with IntelliJ IDEA.
 * User: daveburke
 * Date: 5/7/15
 * Time: 4:20 PM
 */
@Configuration
@EnableConfigurationProperties
@EnableTransactionManagement
@ComponentScan(basePackages = "com.nixmash.springdata.jpa")
@EnableJpaRepositories(basePackages = "com.nixmash.springdata.jpa")
@PropertySource("classpath:/META-INF/spring/application.properties")
public class ApplicationConfig {


    @Bean
    DateTimeService currentTimeDateTimeService() {
        return new CurrentTimeDateTimeService();
    }

//    @Autowired
//    SiteOptionRepository siteOptionRepository;
//
//    @Bean(name="siteOptionsBean")
//    @DependsOn("siteOptionRepository")
//    public SiteOptions siteOptions() throws
//            IllegalAccessException, NoSuchMethodException, InvocationTargetException {
//        SiteOptions bean = new SiteOptions();
//        Collection<SiteOption> siteOptionKeyValues = siteOptionRepository.findAll();
//        Map<String,Object> options = new Hashtable<>();
//        for (SiteOption siteOption : siteOptionKeyValues) {
//            options.put(siteOption.getName(), siteOption.getValue());
//        }
//
//        for (String key : options.keySet()) {
//            for (Field f : bean.getClass().getDeclaredFields()) {
//                if (f.getName().toUpperCase().equals(key.toUpperCase())) {
//                    bean.setSiteOptionProperty(key, options.with(key));
//                }
//            }
//        }
//        return bean;
//
//    }
}