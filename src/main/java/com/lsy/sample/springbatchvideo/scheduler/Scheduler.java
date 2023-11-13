package com.lsy.sample.springbatchvideo.scheduler;

import com.lsy.sample.springbatchvideo.batch.JpaPageJob1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class Scheduler {

    private final JobLauncher jobLauncher;
    private final Job JpaPageJob2_batchBuild;

    @Scheduled(cron = "0 20 18 * * *")
    public void schedule1() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        jobLauncher.run(JpaPageJob2_batchBuild, new JobParametersBuilder()
                .addString("date", LocalDateTime.now().toString())
                .toJobParameters()
        );
    }
}
