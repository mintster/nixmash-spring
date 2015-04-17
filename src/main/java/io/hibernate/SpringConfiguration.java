package io.hibernate;

import io.hibernate.model.Contact;
import io.hibernate.model.ContactTelDetail;
import io.hibernate.model.Hobby;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties
@EnableTransactionManagement
@ComponentScan(basePackages = "io.hibernate")
@PropertySource("classpath:application.properties")
public class SpringConfiguration {

    private
    @Value("${mysql.username}")
    String username;

    @Autowired
    private Environment env;

    @Autowired
    private SpringProperties springProperties;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();

        Resource[] resources = new ClassPathResource[]{
                new ClassPathResource("application.properties")};

        pspc.setLocations(resources);
        pspc.setIgnoreResourceNotFound(true);
        pspc.setIgnoreUnresolvablePlaceholders(true);
        pspc.setLocalOverride(true);
        return pspc;
    }

    @Bean
    public SessionFactory sessionFactory() {
        return new LocalSessionFactoryBuilder(getDataSource())
                .addAnnotatedClasses(Contact.class)
                .addAnnotatedClasses(ContactTelDetail.class)
                .addAnnotatedClasses(Hobby.class)
                .buildSessionFactory();
    }

    // region v0.0.2 printProperties()

//    @PostConstruct
//    public void printProperties() {
//        SpringUtils.printProperty("env.getProperty(\"token\")", env.getProperty("token"));
//        SpringUtils.printProperty("springProperties.getToken()", springProperties.getToken());
//        SpringUtils.printProperty("username", username);
//    }

    // endregion

    @Bean
    public DataSource getDataSource() {

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/" + env.getProperty("mysql.database"));
        dataSource.setUsername(env.getProperty("mysql.username"));
        dataSource.setPassword(env.getProperty("mysql.password"));
        return dataSource;
    }

    @Bean
    public HibernateTransactionManager hibTransMan() {
        return new HibernateTransactionManager(sessionFactory());
    }


}