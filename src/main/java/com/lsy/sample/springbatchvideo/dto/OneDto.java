package com.lsy.sample.springbatchvideo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//한줄에 읽는 파일?
public class OneDto {

    private String one;

    @Override
    public String toString() {
        return one;
    }
}
