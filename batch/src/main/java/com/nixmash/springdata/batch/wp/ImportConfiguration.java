package com.nixmash.springdata.batch.wp;


import com.nixmash.springdata.jpa.model.Post;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableBatchProcessing
@ComponentScan(basePackages = {"com.nixmash.springdata.batch", "com.nixmash.springdata.jpa"})
public class ImportConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public JpaPagingItemReader<Post> reader() throws Exception {
        String jpqlQuery = "select p from Post p";

        JpaPagingItemReader<Post> reader = new JpaPagingItemReader<>();
        reader.setQueryString(jpqlQuery);
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setPageSize(1000);
        reader.afterPropertiesSet();
        reader.setSaveState(false);

        return reader;
    }

    @Bean
    public PostItemProcessor processor() {
        return new PostItemProcessor();
    }

    @Bean
    public FlatFileItemWriter<Post> writer() {
        FlatFileItemWriter<Post> writer = new FlatFileItemWriter<Post>();
        writer.setResource(new ClassPathResource("/resources/post.cvs"));
        DelimitedLineAggregator<Post> delLineAgg = new DelimitedLineAggregator<Post>();
        delLineAgg.setDelimiter(",");
        BeanWrapperFieldExtractor<Post> fieldExtractor = new BeanWrapperFieldExtractor<Post>();
        fieldExtractor.setNames(new String[] {"postTitle"});
        delLineAgg.setFieldExtractor(fieldExtractor);
        writer.setLineAggregator(delLineAgg);
        return writer;
    }

    @Bean
    public Job importPostJob(PostImportCompletionListener listener) throws Exception {
        return jobBuilderFactory.get("importPostJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }


    @Bean
    public Step step1() throws Exception {
        return stepBuilderFactory.get("step1")
                .<Post, Post> chunk(20)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
}
