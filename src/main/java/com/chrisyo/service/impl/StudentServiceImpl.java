package com.chrisyo.service.impl;
import com.chrisyo.entity.PageBean;
import com.chrisyo.entity.Student;
import com.chrisyo.entity.StudentQueryParams;
import com.chrisyo.mapper.StudentMapper;
import com.chrisyo.service.StudentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentMapper studentMapper;

    @Override
    public PageBean page(StudentQueryParams stuQueryParam) {
        PageHelper.startPage(stuQueryParam.getPage(), stuQueryParam.getPageSize());
        List<Student> studentList = studentMapper.page(stuQueryParam);
        Page<Student> p = (Page<Student>) studentList;
        return new PageBean(p.getTotal(), p.getResult());
    }

    @Override
    public void delete(List<Integer> ids) {
        studentMapper.delete(ids);
    }

    @Override
    public void add(Student stuObj) {
        stuObj.setUpdateTime(LocalDateTime.now());
        stuObj.setCreateTime(LocalDateTime.now());
        stuObj.setViolationCount((short) 0);
        stuObj.setViolationScore((short) 0);
        studentMapper.add(stuObj);
    }

    @Override
    public Student getById(Integer id) {
        return studentMapper.getById(id);

    }

    @Override
    public void update(Student stuObj) {
        stuObj.setUpdateTime(LocalDateTime.now());
        studentMapper.update(stuObj);

    }

    @Override
    public void updateViolation(Integer id, Integer score) {
        studentMapper.updateViolation(id, score);


    }


}
