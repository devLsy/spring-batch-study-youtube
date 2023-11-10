package com.lsy.sample.springbatchvideo.batch;

import com.lsy.sample.springbatchvideo.dto.OneDto;
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
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class TextJob2 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static final int chunkSize = 5;

    @Bean
    public Job textjob2_batchBuild() {
        return jobBuilderFactory.get("textjob2")
                .start(textjob2_batchStep1())
                .build();
    }


    @Bean
    public Step textjob2_batchStep1() {
        return stepBuilderFactory.get("testJob1_batchStep1")
                .<OneDto, OneDto>chunk(chunkSize)
                .reader(textjob2_FileReader())
                .processor(text2_processor())
                .writer(textjob2_FileWriter())
                .build();
    }

    //가공
    @Bean
    public ItemProcessor<OneDto, OneDto> text2_processor() {
        //어벤저스 문구 추가
        return OneDto -> {
            OneDto.setOne(OneDto.getOne().toString() + " ==> Avengers");
            return OneDto;
        };
    }

    // text reader
    @Bean
    public FlatFileItemReader<OneDto> textjob2_FileReader() {
        FlatFileItemReader<OneDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("sample/textjob2_input.txt"));
        flatFileItemReader.setLineMapper((line, lineNumber) -> new OneDto(lineNumber + ". " + line));
        return flatFileItemReader;
    }

    // text writer
    public FlatFileItemWriter<OneDto> textjob2_FileWriter() {
        return new FlatFileItemWriterBuilder<OneDto>()
                .name("textJob2_FileWriter")
                .resource(new FileSystemResource("output/textjob2_output.txt"))
                .lineAggregator(new PassThroughLineAggregator<>())
                .build();
    }
}
