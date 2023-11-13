package com.lsy.sample.springbatchvideo.batch.item;

import com.lsy.sample.springbatchvideo.service.DeptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j
@RequiredArgsConstructor
public class BatchTestTaskLet implements Tasklet, StepExecutionListener {

    private final DeptService deptService;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.debug("beforeStep~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        //비지니스 로직 수행
        deptService.getDeptList();
        return RepeatStatus.FINISHED;
    }
    
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.debug("end Job~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        return ExitStatus.COMPLETED;
    }
}
