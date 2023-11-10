package com.lsy.sample.springbatchvideo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//구분자가 있는 파일 읽기 위함
public class TwoDto {

    private String one;
    private String two;

    @Override
    public String toString() {
        return one + two;
    }
}
