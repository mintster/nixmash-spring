package com.nixmash.springdata.jpa.config.db;

import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;
import java.util.Properties;

import static java.lang.Boolean.TRUE;
import static org.hibernate.cfg.AvailableSettings.*;
import static org.hibernate.jpa.AvailableSettings.NAMING_STRATEGY;

@Configuration
@PropertySource("classpath:/META-INF/spring/mysql.properties")
@Profile(DataConfigProfile.MYSQL)
public class MySqlConfig extends JpaCommonConfig {

    @Override
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
        return MySQL5InnoDBDialect.class;
    }


}

