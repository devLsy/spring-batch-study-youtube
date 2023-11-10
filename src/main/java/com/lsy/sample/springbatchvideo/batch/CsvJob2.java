package com.lsy.sample.springbatchvideo.batch;

import com.lsy.sample.springbatchvideo.dto.TwoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class CsvJob2 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static final int chunkSize = 5;

    @Bean
    public Job csvJob2_batchBuild() {
        return jobBuilderFactory.get("csvJob2")
                .start(csvJob2_batchStep())
                .build();
    }

    @Bean
    public Step csvJob2_batchStep() {
        return stepBuilderFactory.get("csvJob2_batchStep")
                .<TwoDto, TwoDto>chunk(chunkSize)
                .reader(csvJob2_FileReader())
                .processor(csvJob1_processor())
                .writer(csvJob1_FileWriter())
                .build();
    }

    @Bean
    public ItemProcessor<TwoDto, TwoDto> csvJob1_processor() {
        return TwoDto -> {
            TwoDto.setOne(TwoDto.getOne().toString() + " @ ");
            return TwoDto;
        };
    }

    // text reader
    @Bean
    public FlatFileItemReader<TwoDto> csvJob2_FileReader() {
        FlatFileItemReader<TwoDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("sample/csvJob1_input.csv"));
        //1번 째 라인 스킵
        flatFileItemReader.setLinesToSkip(1);

        DefaultLineMapper<TwoDto> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames("one", "two");
        delimitedLineTokenizer.setDelimiter(":");

        BeanWrapperFieldSetMapper<TwoDto> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(TwoDto.class);

        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
        flatFileItemReader.setLineMapper(defaultLineMapper);
        return flatFileItemReader;
    }   

    // text writer
    @Bean
    public FlatFileItemWriter<TwoDto> csvJob1_FileWriter() {
        return new FlatFileItemWriterBuilder<TwoDto>()
                .name("csvJob1_FileWriter")
                .resource(new FileSystemResource("output/csvJob2_output.csv"))
                .lineAggregator(new PassThroughLineAggregator<>())
                .build();
    }
}
