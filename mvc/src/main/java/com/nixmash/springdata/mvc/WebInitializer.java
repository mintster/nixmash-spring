package com.nixmash.springdata.mvc;

import com.nixmash.springdata.mvc.config.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;


@SpringBootApplication
public class WebInitializer extends SpringBootServletInitializer {

    private static final String DISPATCHER_SERVLET_NAME = "dispatcher";
    private static final String DISPATCHER_SERVLET_MAPPING = "/";

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebInitializer.class);
    }


    public static void main(String[] args) {
        SpringApplication.run(WebInitializer.class, args);
    }


    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        //Loading application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(WebConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(rootContext);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);

        //Dispatcher servlet
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet(DISPATCHER_SERVLET_NAME, new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping(DISPATCHER_SERVLET_MAPPING);

        //Context loader listener
        servletContext.addListener(new ContextLoaderListener(rootContext));
    }

}


