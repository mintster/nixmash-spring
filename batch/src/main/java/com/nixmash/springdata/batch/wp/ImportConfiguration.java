package com.nixmash.springdata.batch.wp;


import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.persistence.EntityManagerFactory;

@Configuration
public class ImportConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ImportConfiguration.class);


    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public PostImportCompletionListener postImportCompletionListener;

    @Autowired
    public JobLauncher jobLauncher;

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
    public PostItemProcessor processor() {
        return new PostItemProcessor();
    }

    @Bean
    public ItemWriter<PostDTO> writer() {
        FlatFileItemWriter<PostDTO> writer = new FlatFileItemWriter<PostDTO>();
        writer.setResource(new FileSystemResource("/home/daveburke/web/nixmashspring/posts-out.csv"));
        DelimitedLineAggregator<PostDTO> delLineAgg = new DelimitedLineAggregator<PostDTO>();
        delLineAgg.setDelimiter(";");
        BeanWrapperFieldExtractor<PostDTO> fieldExtractor = new BeanWrapperFieldExtractor<PostDTO>();
        fieldExtractor.setNames(new String[]{"postTitle"});
        delLineAgg.setFieldExtractor(fieldExtractor);
        writer.setLineAggregator(delLineAgg);
        return writer;
    }

    @Bean(name = "importPostJob")
    public Job importPostJob() throws Exception {
        return jobBuilderFactory.get("importPostJob")
                .incrementer(new RunIdIncrementer())
                .listener(postImportCompletionListener)
                .flow(step1())
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
                .allowStartIfComplete(true)
                .build();
    }

//    @Scheduled(fixedRate=5000)
//    public void printMessage() {
//        System.out.println("I am called by Spring scheduler");
//    }


//    @Scheduled(fixedRate = 1000)
//    public void importPosts() throws Exception {
//        JobParameters jobParameters =
//                new JobParametersBuilder()
//                        .addLong("time", System.currentTimeMillis()).toJobParameters();
//        JobExecution execution = jobLauncher.run(importPostJob(), jobParameters);
//            System.out.println("I am called by Spring scheduler");
//    }
}
