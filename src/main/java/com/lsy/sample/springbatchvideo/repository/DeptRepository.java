package com.lsy.sample.springbatchvideo.repository;

import com.lsy.sample.springbatchvideo.domain.Dept;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeptRepository extends CrudRepository<Dept, Long> {

    @Override
    List<Dept> findAll();
}
