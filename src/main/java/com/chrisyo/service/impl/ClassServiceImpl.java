package com.chrisyo.service.impl;

import com.chrisyo.entity.Class;
import com.chrisyo.entity.ClassQueryParam;
import com.chrisyo.entity.PageBean;
import com.chrisyo.service.ClassService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {


    @Override
    public PageBean page(ClassQueryParam classQueryParam) {
        return null;
    }

    @Override
    public void delete(List<Integer> ids) {

    }

    @Override
    public void add(Class classObj) {

    }

    @Override
    public Class getClassById(Integer id) {
        return null;
    }

    @Override
    public void update(Class classObj) {

    }

    @Override
    public List<Class> getAll() {
        return List.of();
    }
}
