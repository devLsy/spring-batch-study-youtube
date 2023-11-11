package com.lsy.sample.springbatchvideo.batch;

import com.lsy.sample.springbatchvideo.dto.TwoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class CsvJob1 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static final int chunkSize = 5;

    @Bean
    public Job csvJob1_batchBuild() {
        return jobBuilderFactory.get("csvJob1")
                .start(csvJob1_batchStep())
                .build();
    }

    @Bean
    public Step csvJob1_batchStep() {
        return stepBuilderFactory.get("csvJob1_batchStep")
                .<TwoDto, TwoDto>chunk(chunkSize)
                .reader(csvJob1_FileReader())
                .writer(twoDto -> twoDto.forEach(twoDto2 -> {
                    log.debug(twoDto2.toString());
                }))
                .build();
    }

    // text reader
    @Bean
    public FlatFileItemReader<TwoDto> csvJob1_FileReader() {
        FlatFileItemReader<TwoDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("sample/csvJob1_input.csv"));
        //1번 째 라인 스킵
        flatFileItemReader.setLinesToSkip(1);
        //라인을 읽기 위한 객체
        DefaultLineMapper<TwoDto> defaultLineMapper = new DefaultLineMapper<>();
        //구분자를 위한 객체
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        //Dto에 있는 필드와 매핑
        delimitedLineTokenizer.setNames("one", "two");
        //구분자를 기준으로 데이터를 자를거야
        delimitedLineTokenizer.setDelimiter(":");
        //데이터를 넣기 위한 객체
        BeanWrapperFieldSetMapper<TwoDto> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        //TwoDto가 타깃이야
        beanWrapperFieldSetMapper.setTargetType(TwoDto.class);
        //구분자 설정
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        //어떤 필드에 넣을지 설정
        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
        //구분자와 필드를 설정한 객체를 세팅
        flatFileItemReader.setLineMapper(defaultLineMapper);
        //최종적으로 Dto를 담은 객체를 반환
        return flatFileItemReader;
    }
}
