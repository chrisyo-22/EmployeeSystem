package com.chrisyo.service;

import com.chrisyo.entity.*;

import java.util.List;

public interface EmpService {

    PageBean page(EmpQueryParam empQueryParam);

    /**
     * Create a new employee
     * @param employee employee info
     */
    void create(Employee employee);

    void delete(List<Integer> ids);

    Employee getEmpById(Integer id);

    void update(Employee employee);

    LoginInfo login(Employee emp);
}
