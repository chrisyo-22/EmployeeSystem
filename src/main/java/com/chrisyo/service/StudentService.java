package com.chrisyo.service;


import com.chrisyo.entity.PageBean;
import com.chrisyo.entity.Student;
import com.chrisyo.entity.StudentQueryParams;

import java.util.List;

public interface StudentService {

    void delete(List<Integer> id);

    void add(Student stuObj);

    Student getById(Integer id);

    void update(Student stuObj);

    void updateViolation(Integer id, Integer score);

    PageBean page(StudentQueryParams stuQueryParam);
}
