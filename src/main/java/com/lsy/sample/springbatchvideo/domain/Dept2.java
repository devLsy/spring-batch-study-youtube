package com.lsy.sample.springbatchvideo.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Dept2 {

    @Id
    Integer deptNo;
    String dName;
    String loc;
}
