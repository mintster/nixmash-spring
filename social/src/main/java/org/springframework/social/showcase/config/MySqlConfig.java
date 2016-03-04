package org.springframework.social.showcase.config;

import static java.lang.Boolean.TRUE;
import static org.hibernate.cfg.AvailableSettings.FORMAT_SQL;
import static org.hibernate.cfg.AvailableSettings.GENERATE_STATISTICS;
import static org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO;
import static org.hibernate.cfg.AvailableSettings.SHOW_SQL;
import static org.hibernate.cfg.AvailableSettings.USE_SQL_COMMENTS;
import static org.hibernate.jpa.AvailableSettings.NAMING_STRATEGY;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.nixmash.springdata.jpa.model.auditors.AuditingDateTimeProvider;
import com.nixmash.springdata.jpa.model.auditors.DateTimeService;
import com.nixmash.springdata.jpa.model.auditors.UsernameAuditorAware;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
@EnableSpringDataWebSupport
@PropertySource("classpath:/mysql.properties")
public abstract class MySqlConfig {

    // region Constants

    private static final Logger logger = LoggerFactory.getLogger(MySqlConfig.class);
    public static final String UNDEFINED = "**UNDEFINED**";
    public static final String CONNECTION_CHAR_SET = "hibernate.connection.charSet";
    public static final String VALIDATOR_APPLY_TO_DDL = "hibernate.validator.apply_to_ddl";
    public static final String VALIDATOR_AUTOREGISTER_LISTENERS = "hibernate.validator.autoregister_listeners";

    // endregion

    @Autowired
    protected Environment environment;

    @Value("#{ environment['entity.package'] }")
    private String entityPackage;

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(getDriverClassName());
        dataSource.setUrl(getUrl());
        dataSource.setUsername(getUser());
        dataSource.setPassword(getPassword());
        dataSource.setValidationQuery(getDatabaseValidationQuery());
        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(true);
        dataSource.setTestWhileIdle(true);
        dataSource.setTimeBetweenEvictionRunsMillis(1800000);
        dataSource.setNumTestsPerEvictionRun(3);
        dataSource.setMinEvictableIdleTimeMillis(1800000);
        return dataSource;
    }

    @Bean
    AuditorAware<String> auditorProvider() {
        return new UsernameAuditorAware();
    }

    @Bean
    DateTimeProvider dateTimeProvider(DateTimeService dateTimeService) {
        return new AuditingDateTimeProvider(dateTimeService);
    }

    @Bean
    @Qualifier(value = "jpaTransactionManager")
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        logger.debug("\n\n************ {} ************\n\n",
                getDatabaseDialect().getCanonicalName());
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setDatabasePlatform(getDatabaseDialect().getName());
        vendorAdapter.setShowSql(true);

        LocalContainerEntityManagerFactoryBean factory =
                new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        logger.debug("\n\n****** Scanning '{}' Packages for Entities ******\n\n", entityPackage);
        factory.setPackagesToScan(entityPackage);
        factory.setDataSource(dataSource());
        if (getJpaProperties() != null) {
            factory.setJpaProperties(getJpaProperties());
        }
        return factory;
    }

    protected Class<? extends Dialect> getDatabaseDialect() {
        return MySQL5InnoDBDialect.class;
    }

    protected Properties getJpaProperties() {
        Properties properties = new Properties();
        properties.setProperty(HBM2DDL_AUTO, getHbm2ddl());
        properties.setProperty(GENERATE_STATISTICS, TRUE.toString());
        properties.setProperty(SHOW_SQL, getShowSql());
        properties.setProperty(FORMAT_SQL, TRUE.toString());
        properties.setProperty(USE_SQL_COMMENTS, TRUE.toString());
        properties.setProperty(CONNECTION_CHAR_SET, getHibernateCharSet());
        properties.setProperty(NAMING_STRATEGY, ImprovedNamingStrategy.class.getName());
        return properties;
    }

    // region Get Properties from datasource .properties file

    public String getDatabaseName() {
        return environment.getProperty("database.name", UNDEFINED);
    }

    public String getHost() {
        return environment.getProperty("database.host", UNDEFINED);
    }

    public String getPort() {
        return environment.getProperty("database.port", UNDEFINED);
    }

    public String getUrl() {
        return environment.getProperty("database.url", UNDEFINED);
    }

    public String getUser() {
        return environment.getProperty("database.username", UNDEFINED);
    }

    public String getPassword() {
        return environment.getProperty("database.password", UNDEFINED);
    }

    public String getDriverClassName() {
        return environment.getProperty("database.driverClassName", UNDEFINED);
    }

    public String getDialect() {
        return environment.getProperty("database.dialect", UNDEFINED);
    }

    public String getDatabaseVendor() {
        return environment.getProperty("database.vendor", UNDEFINED);
    }

    public String getHbm2ddl() {
        return environment.getProperty("database.hbm2ddl.auto", "none");
    }

    public String getShowSql() {
        return environment.getProperty("hibernate.showsql", "TRUE");
    }


    public String getHibernateCharSet() {
        return environment.getProperty("database.hibernateCharSet", "UTF-8");
    }

    public String getDatabaseValidationQuery() {
        return environment.getProperty("database.validation.query", UNDEFINED);
    }
}
