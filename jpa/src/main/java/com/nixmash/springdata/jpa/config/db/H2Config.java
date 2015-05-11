package com.nixmash.springdata.jpa.config.db;

import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.HSQLDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

import static java.lang.Boolean.TRUE;
import static org.hibernate.cfg.AvailableSettings.*;
import static org.hibernate.jpa.AvailableSettings.NAMING_STRATEGY;

@Configuration
@PropertySource("classpath:/META-INF/spring/h2database.properties")
@Profile(DataConfigProfile.H2)
public class H2Config extends JpaCommonConfig {


    @Override
    @Bean(destroyMethod = "close")
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

    @Override
    protected Properties getJpaProperties() {
        Properties properties = new Properties();
        properties.setProperty(HBM2DDL_AUTO, getHbm2ddl());
        properties.setProperty(GENERATE_STATISTICS, TRUE.toString());
        properties.setProperty(SHOW_SQL, TRUE.toString());
        properties.setProperty(FORMAT_SQL, TRUE.toString());
        properties.setProperty(USE_SQL_COMMENTS, TRUE.toString());
        properties.setProperty(CONNECTION_CHAR_SET, getHibernateCharSet());
        properties.setProperty(NAMING_STRATEGY, ImprovedNamingStrategy.class.getName());
        return properties;
    }


    @Override
    protected Class<? extends Dialect> getDatabaseDialect() {
        return HSQLDialect.class;
    }

    @Bean
    public DatabasePopulator databasePopulator(DataSource dataSource) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.setContinueOnError(true);
        populator.setIgnoreFailedDrops(true);
        populator.addScript(new ClassPathResource("/db/h2database.sql"));
        try {
            populator.populate(dataSource.getConnection());
        } catch (SQLException ignored) {
        }
        return populator;
    }




}
