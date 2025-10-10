package com.chrisyo.mapper;

import com.chrisyo.entity.Student;
import com.chrisyo.entity.StudentQueryParams;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentMapper {

    List<Student> page(StudentQueryParams stuQueryParam);

    void delete(List<Integer> ids);

    Student getById(Integer id);

    void add(Student stuObj);

    void update(Student stuObj);

    void updateViolation(Integer id, Integer score);
}
