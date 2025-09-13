package com.chrisyo.service.impl;

import com.chrisyo.entity.Class;
import com.chrisyo.entity.ClassQueryParam;
import com.chrisyo.entity.Employee;
import com.chrisyo.entity.PageBean;
import com.chrisyo.mapper.ClassMapper;
import com.chrisyo.service.ClassService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    ClassMapper classMapper;

    @Override
    public PageBean page(ClassQueryParam classQueryParam) {
        PageHelper.startPage(classQueryParam.getPage(), classQueryParam.getPageSize());
        List<Class> classes = classMapper.list(classQueryParam);

        //Fill the class's status based on the begin_date and end_date
        classes.forEach(c -> {
            if (c.getBeginDate().isAfter(LocalDate.now())) {
                c.setStatus("Not Active");
            } else if (c.getEndDate().isAfter(LocalDate.now())) {
                c.setStatus("Active");
            } else {
                c.setStatus("Expired");
            }
        });
        Page<Class> p = (Page<Class>) classes;
        return new PageBean(p.getTotal(), p.getResult());
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        //1. Wipe all student's classId matching this id
        classMapper.deleteStudentClassId(id);
        //2. Delete class based on id
        classMapper.delete(id);
    }

    @Override
    @Transactional
    public void add(Class classObj) {
        classObj.setUpdateTime(LocalDateTime.now());
        classObj.setCreateTime(LocalDateTime.now());
        classMapper.add(classObj);
    }

    @Override
    public Class getClassById(Integer id) {
        return classMapper.getClassById(id);
    }

    @Override
    public void update(Class classObj) {
        classMapper.update(classObj);
    }

    @Override
    public List<Class> getAll() {
        return classMapper.list(new ClassQueryParam());
    }
}
