package com.nixmash.springdata.mvc.config;

import com.nixmash.springdata.jpa.exceptions.ContactNotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: daveburke
 * Date: 6/23/15
 * Time: 3:06 PM
 */

public class ExceptionConfig {

    SimpleMappingExceptionResolver getSimpleMappingExceptionResolver() {

        SimpleMappingExceptionResolver result
                = new SimpleMappingExceptionResolver();

        // Setting customized exception mappings
        Properties p = new Properties();
        p.put(ContactNotFoundException.class.getName(), "error");
        result.setExceptionMappings(p);

        // Un-mapped exceptions will be directed there
        result.setDefaultErrorView("error");

        // Setting a default HTTP status code
        result.setDefaultStatusCode(HttpStatus.BAD_REQUEST.value());

        return result;

    }

    @Bean
    HandlerExceptionResolverComposite getHandlerExceptionResolverComposite() {

        HandlerExceptionResolverComposite result
                = new HandlerExceptionResolverComposite();

        List<HandlerExceptionResolver> l
                = new ArrayList<>();

        l.add(new ExceptionHandlerExceptionResolver());
        l.add(new ResponseStatusExceptionResolver());
        l.add(getSimpleMappingExceptionResolver());
        l.add(new DefaultHandlerExceptionResolver());

        result.setExceptionResolvers(l);

        return result;

    }

}
