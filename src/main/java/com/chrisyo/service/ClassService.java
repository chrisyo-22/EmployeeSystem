package com.chrisyo.service;

import com.chrisyo.entity.Class;
import com.chrisyo.entity.ClassQueryParam;
import com.chrisyo.entity.PageBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClassService {

    PageBean page(ClassQueryParam classQueryParam);

    void delete(Integer id);

    void add(Class classObj);

    Class getClassById(Integer id);

    void update(Class classObj);

    List<Class> getAll();
}
