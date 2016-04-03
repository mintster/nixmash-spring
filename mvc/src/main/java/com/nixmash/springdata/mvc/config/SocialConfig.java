package com.nixmash.springdata.mvc.config;


import com.nixmash.springdata.jpa.common.ApplicationSettings;
import com.nixmash.springdata.mvc.security.SocialSignInAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import javax.sql.DataSource;

@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ApplicationSettings appSettings;

    @Autowired
    private Environment environment;

    @Value("#{ environment['spring.social.application.url'] }")
    private String applicationUrl;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
        cfConfig.addConnectionFactory(new TwitterConnectionFactory(appSettings.getTwitterAppId(),
                appSettings.getTwitterAppSecret()));
        cfConfig.addConnectionFactory(new FacebookConnectionFactory(appSettings.getFacebookAppId(),
                appSettings.getFacebookAppSecret()));
        cfConfig.addConnectionFactory(new GoogleConnectionFactory(appSettings.getGoogleAppId(),
                appSettings.getGoogleAppSecret()));
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
    }

    @Bean
    public SignInAdapter signInAdapter() {
        return new SocialSignInAdapter(new HttpSessionRequestCache());
    }

    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator,
                                                   UsersConnectionRepository connectionRepository) {
        return new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
    }

    ;

    @Bean
    public ProviderSignInController providerSignInController(ConnectionFactoryLocator connectionFactoryLocator,
                                                             UsersConnectionRepository usersConnectionRepository) {
        ProviderSignInController controller = new ProviderSignInController(connectionFactoryLocator,
                usersConnectionRepository, signInAdapter());
        controller.setApplicationUrl(applicationUrl);
        return controller;
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

}
