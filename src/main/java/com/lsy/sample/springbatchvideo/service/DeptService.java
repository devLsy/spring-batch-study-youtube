package com.lsy.sample.springbatchvideo.service;

import com.lsy.sample.springbatchvideo.domain.Dept;
import com.lsy.sample.springbatchvideo.repository.DeptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeptService {

    private final DeptRepository deptRepository;

    /**
     * get dept list
     */
    public void getDeptList() {
        List<Dept> deptList = deptRepository.findAll();
        deptList.forEach(dept -> log.debug("dept ==> [{}]", dept.toString()));
    }
}
