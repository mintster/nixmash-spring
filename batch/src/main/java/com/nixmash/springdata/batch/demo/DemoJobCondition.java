package com.nixmash.springdata.batch.demo;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by daveburke on 11/18/16.
 */
public class DemoJobCondition implements Condition {

    private static final String DEMOJOB = "demojob";

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String job = context.getEnvironment().getProperty("job");
        if (job != null) {
            return job.equalsIgnoreCase(DEMOJOB);
        }
        return false;
    }
}
