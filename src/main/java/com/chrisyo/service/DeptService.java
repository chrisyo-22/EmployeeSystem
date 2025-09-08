package com.chrisyo.service;

import com.chrisyo.entity.Dept;

import java.util.List;

public interface DeptService {
    List<Dept> list();

    void delete(Integer id);

    Dept add(Dept dept);

    Dept getById(Integer id);

    Dept update(Dept dept);
}
