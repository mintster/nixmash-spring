package com.nixmash.springdata.mvc.config;

/**
 * Created with IntelliJ IDEA.
 * User: daveburke
 * Date: 7/16/15
 * Time: 4:40 PM
 */

import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.mvc.security.CurrentUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

import java.util.Collections;

//import org.springframework.security.core.userdetails.User;

@Configuration
@EnableWebSecurity
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private static final String[] IGNORED_RESOURCE_LIST = new String[]{"/resources/**",
            "/static/**", "/webjars/**", "/favicon.ico"};

    private static final String[] PERMITALL_RESOURCE_LIST = new String[]{"/", "/favicon.ico","/contacts/**", "/search/**", "/list.html"};
    private static final String[] USER_ROLE_RESOURCE_LIST = new String[]{"/contact/**"};
    private static final String[] ADMIN_RESOURCE_LIST = new String[]{"/console/**"};

    @Configuration
    protected static class InMemoryPersistentTokenRememberMeSetup {
        @Value("${rememberMeToken}")
        private String rememberMeToken;

        @Value("${rememberMeParameter}")
        private String rememberMeParameter;

        public RememberMeServices getRememberMeServices() {
            PersistentTokenBasedRememberMeServices services = new PersistentTokenBasedRememberMeServices(
                    rememberMeToken, new BasicRememberMeUserDetailsService(),
                    new InMemoryTokenRepositoryImpl());
            services.setParameter(rememberMeParameter);
            return services;
        }

        public class BasicRememberMeUserDetailsService implements UserDetailsService {
            @Override
            public CurrentUser loadUserByUsername(String username) throws UsernameNotFoundException {
                return new CurrentUser(new User(username, "", Collections.<GrantedAuthority>emptyList()));
            }
        }
    }

//    @Order(1)
//    @Configuration
//    @EnableGlobalMethodSecurity(prePostEnabled = true)
//    @Profile(DataConfigProfile.H2)
//    protected static class InternalAuthenticationSecurity extends
//            GlobalAuthenticationConfigurerAdapter {
//
//        @Autowired
//        private UserDetailsService userDetailsService;
//
//        @Override
//        public void init(AuthenticationManagerBuilder auth) throws Exception {
//            //@formatter:off
//            auth
//              .userDetailsService(userDetailsService)
//                  .passwordEncoder(new BCryptPasswordEncoder())
//            ;
//            //@formatter:on
//        }
//
//
//    }

    @Autowired
    private UserDetailsService userDetailsService;


//    @Configuration
//    @Order(1)
//    public static class H2ConfigurationAdapter extends
//            WebSecurityConfigurerAdapter {
//
//        @Override
//        protected void configure(HttpSecurity httpSecurity) throws Exception {
//            httpSecurity
//                    .csrf().disable()
//                    .headers()
//                    .cacheControl()
//                    .contentTypeOptions()
//                    .httpStrictTransportSecurity()
//                    .and()
//                    .requestMatchers()
//                    .antMatchers("/console/**")
//                    .and()
//                    .formLogin()
//                    .loginPage("/login")
//                    .and()
//                    .authorizeRequests()
//                    .anyRequest().hasAuthority("ROLE_ADMIN");
//        }
////
//
////        @Override
////        protected void configure(HttpSecurity httpSecurity) throws Exception {
////            httpSecurity.authorizeRequests().antMatchers("/").permitAll().and()
////                    .authorizeRequests().antMatchers("/console/**").hasRole("ADMIN");
////
////            httpSecurity.csrf().disable();
////            httpSecurity.headers().frameOptions().disable();
////        }
////
////        @Bean
////        @Profile(DataConfigProfile.H2)
////        public ServletRegistrationBean h2servletRegistration() {
////            ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
////            registration.addUrlMappings("/console/*");
////            return registration;
////        }
////
//    }

    @Override
    @Profile(DataConfigProfile.H2)
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //@formatter:off
            web
                .ignoring()
                .antMatchers(IGNORED_RESOURCE_LIST);
            //@formatter:on
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
               http
                         .authorizeRequests()
                      .antMatchers(PERMITALL_RESOURCE_LIST).permitAll()
//                       .antMatchers(ADMIN_RESOURCE_LIST).hasAuthority("ROLE_ADMIN")
                       .antMatchers("/contact/**").fullyAuthenticated()
    .antMatchers("/console/**").hasAuthority("ROLE_ADMIN")
//    .anyRequest().fullyAuthenticated()
        .and()
                .formLogin()
                    .loginPage("/login")
                    .failureUrl("/login?error")
                    .permitAll()
                    .and()
                .logout()
                    .deleteCookies("remember-me")
                    .permitAll()
//                    .and()
//                .rememberMe()
                    .and().exceptionHandling().accessDeniedPage("/403");

//
//                    .and()

            // @formatter:on

        http.csrf().disable();
//        http.headers().frameOptions().disable();
    }
//
//    @Bean
//    public SessionRegistry sessionRegistry() {
//        SessionRegistry sessionRegistry = new SessionRegistryImpl();
//        return sessionRegistry;
//    }
//
//
//    @Bean
//    public SecurityContextPersistenceFilter securityContextPersistenceFilter() {
//        return new SecurityContextPersistenceFilter(
//                new HttpSessionSecurityContextRepository());
//    }
//
//    //     Register HttpSessionEventPublisher
//    @Bean
//    public static ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
//        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
//    }

}