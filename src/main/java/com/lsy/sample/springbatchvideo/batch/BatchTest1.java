package com.lsy.sample.springbatchvideo.batch;

import com.lsy.sample.springbatchvideo.batch.item.BatchTestTaskLet;
import com.lsy.sample.springbatchvideo.service.DeptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchTest1 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DeptService deptService;

    //job
    @Primary
    @Bean
    public Job batchJobTest1() {
        return jobBuilderFactory.get("batchJobTest1")
            .start(batchTestStep())
            .build();
    }

    @Bean
    //step
    public Step batchTestStep() {
        return stepBuilderFactory.get("batchTestStep")
                .tasklet(batchTestLet(deptService))
                .build();
    }

    @Bean
    public Tasklet batchTestLet(DeptService deptService) {
        return new BatchTestTaskLet(deptService);
    }
}
