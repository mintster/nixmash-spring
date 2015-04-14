package io.hibernate;

import io.hibernate.dao.Contact;
import io.hibernate.dao.ContactTelDetail;
import io.hibernate.dao.Hobby;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;


//@ImportResource("app-context.xml")
//
//@PropertySource("classpath:application.properties")

@Configuration
@EnableConfigurationProperties
@EnableTransactionManagement
@ComponentScan(basePackages = "io.hibernate")
@PropertySource("classpath:application.properties")
@ImportResource("app-context.xml")
public class AppConfig {

    private
    @Value("${username}")
    String username;

    @Autowired
    private Environment env;

    @Autowired
    private PropertyClass propertyClass;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        final PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();

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


    @PostConstruct
    public void printProperties() {
        SpringPropertiesUtil.printProperty(
                "env.getProperty(\"username\")",
                env.getProperty("username"));
        SpringPropertiesUtil.printProperty(
                "SpringPropertiesUtil.getProperty(\"username\")",
                SpringPropertiesUtil.getProperty("username"));
        SpringPropertiesUtil.printProperty(
                "@Value(\"${username}\"", username);
        SpringPropertiesUtil.printProperty(
                "propertyClass.getToken()", propertyClass.getToken());
    }

    @Bean
    public DataSource getDataSource() {

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/" + env.getProperty("mysql.database"));
        dataSource.setUsername(username);
        dataSource.setPassword(env.getProperty("mysql.password"));

        return dataSource;
    }

    @Bean
    public HibernateTransactionManager hibTransMan() {
        return new HibernateTransactionManager(sessionFactory());
    }

}