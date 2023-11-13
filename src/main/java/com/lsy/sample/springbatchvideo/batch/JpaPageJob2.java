package com.lsy.sample.springbatchvideo.batch;

import com.lsy.sample.springbatchvideo.domain.Dept;
import com.lsy.sample.springbatchvideo.domain.Dept2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.persistence.EntityManagerFactory;

@Slf4j
@RequiredArgsConstructor
@Configuration
// dept 테이블에서 데이터 읽어와서 "NEW_" 접두어 붙여서 dept2 테이블에 저장하는 예제
public class JpaPageJob2 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    //chunkSize만큼 하기 위함(로직 짜기 나름)
    private int chunkSize = 10000;


    @Primary
    @Bean
    public Job JpaPageJob2_batchBuild() {
        return jobBuilderFactory.get("jpaPageJob2")
                .start(jpaPageJob2_step1())
                .build();
    }

    @Bean
    public Step jpaPageJob2_step1() {
        return stepBuilderFactory.get("jpaPageJob2_step1")
                .<Dept, Dept2>chunk(chunkSize)
                .reader(jpaPageJob2_dbItemReader())
                .processor(jpaPageJob2_processor())
                .writer(jpaPageJob2_dbItemWriter())
                .build();

    }

    private ItemProcessor<Dept, Dept2> jpaPageJob2_processor() {
        return dept -> {
            // 가공한 데이터를 의미
            return new Dept2(dept.getDeptNo(), "NEW_" + dept.getDName(), "NEW_" + dept.getLoc());
        };
    }

    // reader
    @Bean
    public JpaPagingItemReader<Dept> jpaPageJob2_dbItemReader() {
        return new JpaPagingItemReaderBuilder<Dept>()
                .name("jpaPageJob2_dbItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT d FROM Dept d order by dept_no asc")
                .build();

    }

    // writer
    public JpaItemWriter<Dept2> jpaPageJob2_dbItemWriter() {
        JpaItemWriter<Dept2> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }
}
