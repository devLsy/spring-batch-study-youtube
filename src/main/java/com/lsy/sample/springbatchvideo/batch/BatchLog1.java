package com.lsy.sample.springbatchvideo.batch;

import com.lsy.sample.springbatchvideo.domain.Dept;
import com.lsy.sample.springbatchvideo.service.DeptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchLog1 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DeptService deptService;

    @Bean
    public Job batchJob() {
        return jobBuilderFactory.get("batchLogjob")
            .incrementer(new RunIdIncrementer())
            .start(batchStep())
            .build();
    }

    @Bean
    public Step batchStep() {
        return stepBuilderFactory.get("batchStep")
            .tasklet(batchTaskLet())
            .build();
    }

    @Bean
    public Tasklet batchTaskLet() {
        return ((contribution, chunkContext) -> {
            deptService.getDeptList();
            return RepeatStatus.FINISHED;
        });
    }
}
