package com.lsy.sample.springbatchvideo.batch;

import com.lsy.sample.springbatchvideo.dto.MarketDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JsonJob2 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static final int chunkSize = 5;

    //job
    @Bean
    public Job JsonJob2_batchBuild() {
        return jobBuilderFactory.get("JsonJob2_batchBuild")
                .start(JsonJob2_batchStep1())
                .build();
    }

    //step
    @Bean
    public Step JsonJob2_batchStep1() {
        return stepBuilderFactory.get("JsonJob2_batchStep2")
                .<MarketDto, MarketDto>chunk(chunkSize)
                .reader(JsonJob2_jsonReader())
                .processor(jsonJob2_processor())
                .writer((jsonJob2_jsonWriter()))
                .build();
    }

    //가공
    private ItemProcessor<MarketDto, MarketDto> jsonJob2_processor() {
        return MarketDto -> {
            //원화마켓인것만 가져올거야
            if (MarketDto.getMarket().startsWith("KRW-")) {
                return new MarketDto(MarketDto.getMarket(), MarketDto.getKorean_name(), MarketDto.getEnglish_name());
            } else {
                return null;
            }
        };
    }

    //json reader
    @Bean
    public JsonItemReader<MarketDto> JsonJob2_jsonReader() {
        return new JsonItemReaderBuilder<MarketDto>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(MarketDto.class))
                .resource(new ClassPathResource("sample/jsonJob1_input.json"))
                .name("JsonJob2_jsonReader")
                .build();
    }

    //json writer
    @Bean
    public JsonFileItemWriter<MarketDto> jsonJob2_jsonWriter() {
        return new JsonFileItemWriterBuilder<MarketDto>()
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .resource(new FileSystemResource("output/jsonJob2_output.json"))
                .name("jsonJob2_jsonWriter")
                .build();
    }
}
