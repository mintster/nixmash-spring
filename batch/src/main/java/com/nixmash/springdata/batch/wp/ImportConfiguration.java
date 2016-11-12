package com.nixmash.springdata.batch.wp;


import com.nixmash.springdata.jpa.dto.PostDTO;
import com.nixmash.springdata.jpa.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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
import javax.sql.DataSource;

@Configuration
public class ImportConfiguration  {

    private static final Logger logger = LoggerFactory.getLogger(ImportConfiguration.class);


    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public  StepBuilderFactory stepBuilderFactory;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public DataSource dataSource;

    @Autowired
    public PostImportCompletionListener postImportCompletionListener;

//    @Bean
//    public ItemReader<PostDTO> reader() {
//        FlatFileItemReader<PostDTO> reader = new FlatFileItemReader<PostDTO>();
//        reader.setResource(new ClassPathResource("sample-posts.csv"));
//        reader.setLineMapper(new DefaultLineMapper<PostDTO>() {{
//            setLineTokenizer(new DelimitedLineTokenizer() {{
//                setNames(new String[] { "postId", "postTitle" });
//            }});
//            setFieldSetMapper(new BeanWrapperFieldSetMapper<PostDTO>() {{
//                setTargetType(PostDTO.class);
//            }});
//        }});
//        logger.info("In Reader!!!");
//        return reader;
//    }


//    @Bean
//    JdbcPagingItemReader<PostDTO> reader() {
//        JdbcPagingItemReader<PostDTO> databaseReader = new JdbcPagingItemReader<>();
//        databaseReader.setDataSource(dataSource);
//        databaseReader.setPageSize(100);
//        PagingQueryProvider queryProvider = createQueryProvider();
//        databaseReader.setQueryProvider(queryProvider);
//        databaseReader.setRowMapper(new BeanPropertyRowMapper<>(PostDTO.class));
//        return databaseReader;
//    }

//    private PagingQueryProvider createQueryProvider() {
//        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
//
//        queryProvider.setSelectClause("SELECT post_id, post_title");
//        queryProvider.setFromClause("FROM posts");
//        queryProvider.setSortKeys(sortByPostDateDesc());
//
//        return queryProvider;
//    }
//
//    private Map<String, Order> sortByPostDateDesc() {
//        Map<String, Order> sortConfiguration = new HashMap<>();
//        sortConfiguration.put("post_id", Order.DESCENDING);
//        return sortConfiguration;
//    }

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

//    @Bean
//    public ItemProcessor<PostDTO, PostDTO> processor() {
//        return new PostItemProcessor();
//    }
//

    @Bean
    public ItemWriter<PostDTO> writer() {
        FlatFileItemWriter<PostDTO> writer = new FlatFileItemWriter<PostDTO>();
        writer.setResource(new FileSystemResource("/home/daveburke/web/nixmashspring/posts-out.csv"));
        DelimitedLineAggregator<PostDTO> delLineAgg = new DelimitedLineAggregator<PostDTO>();
        delLineAgg.setDelimiter(";");
        BeanWrapperFieldExtractor<PostDTO> fieldExtractor = new BeanWrapperFieldExtractor<PostDTO>();
        fieldExtractor.setNames(new String[] {"postTitle"});
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
                .<Post, PostDTO> chunk(100)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

}
