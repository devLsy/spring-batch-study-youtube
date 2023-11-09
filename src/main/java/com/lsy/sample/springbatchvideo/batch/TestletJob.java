package com.lsy.sample.springbatchvideo.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class TestletJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job taskletJob_batchBuild() {
        return jobBuilderFactory.get("taskletjob")
                .start(taskletJob_step())
                .next(taskletJob_step2(null))
                .build();
    }

    @Bean
    public Step taskletJob_step() {
        return stepBuilderFactory.get("taskletJob_step1")
                .tasklet((a, b) -> {
                    log.info(" ======> job =====> [step1]");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    @JobScope
    public Step taskletJob_step2(@Value("#{jobParameters[date]}") String date) {
        return stepBuilderFactory.get("taskletJob_step2")
                .tasklet((a, b) -> {
                    log.info(" ======> [step1] =====> [step2] [{}]", date);
                    return RepeatStatus.FINISHED;
                }).build();
    }

}
