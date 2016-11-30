package com.nixmash.springdata.batch.github;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by daveburke on 11/30/16.
 */
public class GithubJobCondtion implements Condition {

    private static final String GITHUBJOB = "github";

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String job = context.getEnvironment().getProperty("job");
        if (job != null) {
            return job.equalsIgnoreCase(GITHUBJOB);
        }
        return false;
    }
}