package com.nixmash.springdata.batch.demo;

import com.nixmash.springdata.jpa.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

/**
 * Created by daveburke on 11/10/16.
 */
@Component
public class DemoJobStepListener extends StepExecutionListenerSupport implements ItemReadListener<Post> {

    private static final Logger logger = LoggerFactory.getLogger(DemoJobStepListener.class);

    private ExecutionContext executionContext;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        executionContext = stepExecution.getExecutionContext();
    }

    @Override
    public void beforeRead() {

    }

    @Override
    public void afterRead(Post item) {
        executionContext.put("postId", item.getPostId());
    }

    @Override
    public void onReadError(Exception ex) {

    }
}