package com.nixmash.springdata.mvc.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.security.AuthenticationNameUserIdSource;

import com.nixmash.springdata.mvc.security.SimpleSignInAdapter;


@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {
	
    @Autowired
    private DataSource dataSource;

    
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return new JdbcUsersConnectionRepository (
                dataSource,
                connectionFactoryLocator,
                Encryptors.noOpText() // refer http://stackoverflow.com/questions/12619986/what-is-the-correct-way-to-configure-a-spring-textencryptor-for-use-on-heroku
        );
        
        // Create the table in the database by executing the commands at
        // http://docs.spring.io/spring-social/docs/1.1.0.RELEASE/reference/htmlsingle/#section_jdbcConnectionFactory
        // and then execute
        // create unique index UserConnectionProviderUser on UserConnection(providerId, providerUserId);

    }
    
	@Bean
	public SignInAdapter signInAdapter() {
		return new SimpleSignInAdapter(new HttpSessionRequestCache());
	}

    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository connectionRepository) {
        return new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
    };
	
    @Bean
	public ProviderSignInController providerSignInController(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository) {
    	ProviderSignInController controller = new ProviderSignInController(
    	        connectionFactoryLocator,
    	        usersConnectionRepository,
    	        signInAdapter());
    	return controller;
	}
	
	@Override
	public UserIdSource getUserIdSource() {
		return new AuthenticationNameUserIdSource();
	}

}
