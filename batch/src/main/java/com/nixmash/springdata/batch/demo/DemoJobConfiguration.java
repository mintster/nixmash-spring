package com.nixmash.springdata.batch.demo;


import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.persistence.EntityManagerFactory;

@SuppressWarnings("Convert2Lambda")
@Configuration
public class DemoJobConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DemoJobConfiguration.class);

    private static final FlowExecutionStatus YES = new FlowExecutionStatus("YES");
    private static final FlowExecutionStatus NO = new FlowExecutionStatus("NO");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final DemoJobListener demoJobListener;
    private final DemoJobStepListener demoJobStepListener;

    @Autowired
    public DemoJobConfiguration(EntityManagerFactory entityManagerFactory, DemoJobListener demoJobListener, DemoJobStepListener demoJobStepListener, StepBuilderFactory stepBuilderFactory, JobBuilderFactory jobBuilderFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.demoJobListener = demoJobListener;
        this.demoJobStepListener = demoJobStepListener;
        this.stepBuilderFactory = stepBuilderFactory;
        this.jobBuilderFactory = jobBuilderFactory;
    }

    private String c(FlowExecutionStatus executionStatus) {
        return executionStatus.getName();
    }

    @Bean
    public JpaPagingItemReader<Post> reader() throws Exception {
        String jpqlQuery = "SELECT p from Post p";

        JpaPagingItemReader<Post> reader = new JpaPagingItemReader<>();
        reader.setQueryString(jpqlQuery);
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setPageSize(1000);
        reader.afterPropertiesSet();
        reader.setSaveState(true);

        return reader;
    }

    @Bean
    public DemoJobItemProcessor processor() {
        return new DemoJobItemProcessor();
    }

    @Bean
    public ItemWriter<PostDTO> writer() {
        FlatFileItemWriter<PostDTO> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("/home/daveburke/web/nixmashspring/posts-out.csv"));
        DelimitedLineAggregator<PostDTO> delLineAgg = new DelimitedLineAggregator<>();
        delLineAgg.setDelimiter(";");
        BeanWrapperFieldExtractor<PostDTO> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"postTitle"});
        delLineAgg.setFieldExtractor(fieldExtractor);
        writer.setLineAggregator(delLineAgg);
        return writer;
    }

    @Bean(name = "demoJob")
    public Job demoJob() throws Exception {
        return jobBuilderFactory.get("demoJob")
                .incrementer(new RunIdIncrementer())
                .listener(demoJobListener)
                .flow(step1())
                .next(decideIfGoodToContinue())
                .on(c(NO))
                .end()
                .on(c(YES))
                .to(optionalStep())
                .end()
                .build();
    }

    @Bean
    public Step step1() throws Exception {
        return stepBuilderFactory.get("step1")
                .<Post, PostDTO>chunk(100)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .listener(promotionListener())
                .listener(demoJobStepListener)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public JobExecutionDecider decideIfGoodToContinue() {
        return new JobExecutionDecider() {

            int iteration = 0;

            @Override
            public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
                long postId = 0;
                try {
                    postId = jobExecution.getExecutionContext().getLong("postId");
                } catch (Exception e) {
                    logger.info("Should not display, as all ExecutionContext keys are shared among steps");
                }

                long iterations = jobExecution.getJobParameters().getLong("iterations");
                if(iteration < iterations) {
                    logger.info("ITERATING... POSTID = " + postId);
                    iteration++;
                    return YES;
                } else {
                    logger.info("REPEATED 2X's. SKIPPING OPTIONAL STEP");
                    return NO;
                }
            }
        };
    }

    @Bean
    public Step optionalStep() {
        return stepBuilderFactory.get("optionalStep")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        logger.info("IN OPTIONAL STEP ------------------------ */");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @Bean
    public ExecutionContextPromotionListener promotionListener()
    {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
        listener.setKeys( new String[] { "postId" } );
        return listener;
    }
}
