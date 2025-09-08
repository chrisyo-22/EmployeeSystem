package com.chrisyo.service;

import com.chrisyo.entity.Dept;
import com.chrisyo.entity.EmpQueryParam;
import com.chrisyo.entity.Employee;
import com.chrisyo.entity.PageBean;

import java.util.List;

public interface EmpService {

    PageBean page(EmpQueryParam empQueryParam);

    /**
     * Create a new employee
     * @param employee employee info
     */
    void create(Employee employee);

    void delete(List<Integer> ids);
}
