package com.lsy.sample.springbatchvideo.batch;

import com.lsy.sample.springbatchvideo.dto.ThreeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class FixedLengthJob1 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static final int chunkSize = 5;

    //job
    @Bean
    public Job fixedLengthJob1_batchBuild() {
        return jobBuilderFactory.get("fixedLengthJob1")
                .start(fixedLengthJob1_batchStep())
                .build();
    }

    //step
    @Bean
    public Step fixedLengthJob1_batchStep() {
        return stepBuilderFactory.get("fixedLengthJob1_batchStep")
                .<ThreeDto, ThreeDto>chunk(chunkSize)
                .reader(fixedLengthJob1_FileReader())
                .writer(threeDto -> threeDto.forEach(i -> {
                    log.debug(threeDto.toString());
                }))
                .build();
    }

    // text reader
    @Bean
    public FlatFileItemReader<ThreeDto> fixedLengthJob1_FileReader() {

        FlatFileItemReader<ThreeDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("sample/fixedLengthJob1_input.txt"));
        flatFileItemReader.setLinesToSkip(1);

        DefaultLineMapper<ThreeDto> defaultLineMapper = new DefaultLineMapper<>();

        FixedLengthTokenizer fixedLengthTokenizer = new FixedLengthTokenizer();

        fixedLengthTokenizer.setNames("one", "two");
//        fixedLengthTokenizer.setColumns(new Range[]{new Range(1, 5), new Range(6, 10)});
        fixedLengthTokenizer.setColumns(new Range(1, 5), new Range(6, 10));

        BeanWrapperFieldSetMapper<ThreeDto> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(ThreeDto.class);

        defaultLineMapper.setLineTokenizer(fixedLengthTokenizer);
        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

        flatFileItemReader.setLineMapper(defaultLineMapper);
        return flatFileItemReader;
    }
}
