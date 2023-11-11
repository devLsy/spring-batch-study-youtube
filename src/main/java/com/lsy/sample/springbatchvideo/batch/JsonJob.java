package com.lsy.sample.springbatchvideo.batch;

import com.lsy.sample.springbatchvideo.dto.MarketDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JsonJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static final int chunkSize = 5;

    //job
    @Bean
    public Job jsonJob1_batchBuild() {
        return jobBuilderFactory.get("jsonJob1_batchBuild")
                .start(jsonJob1_batchStep1())
                .build();
    }

    //step
    @Bean
    public Step jsonJob1_batchStep1() {
        return stepBuilderFactory.get("jsonJob1_batchStep1")
                .<MarketDto, MarketDto>chunk(chunkSize)
                .reader(jsonJob1_jsonReader())
                .writer(ColorDto -> ColorDto.forEach(colorDto -> {
                    log.debug(colorDto.toString());
                }))
                .build();
    }

    //json reader
    @Bean
    public JsonItemReader<MarketDto> jsonJob1_jsonReader() {
        return new JsonItemReaderBuilder<MarketDto>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(MarketDto.class))
                .resource(new ClassPathResource("sample/jsonJob1_input.json"))
                .name("jsonJob1_jsonReader")
                .build();
    }
}
