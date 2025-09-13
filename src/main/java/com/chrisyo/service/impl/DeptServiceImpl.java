package com.chrisyo.service.impl;

import com.chrisyo.entity.Dept;
import com.chrisyo.mapper.DeptMapper;
import com.chrisyo.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service Logistics
 */
@Slf4j
@Primary //If there are multiple bean objects, use this first.
@Service("service1")
//Name of the object inside IOC container, if not specify, then default is the name of the class but the first letter in lowercase(eg.deptService)
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;

    @Override
    public List<Dept> list() {
        log.info("Starting to retrieve all departments");
        try {
            //1. invoke mapper's method to get data and return
            List<Dept> departments = deptMapper.list();
            log.info("Successfully retrieved {} departments", departments.size());
            return departments;
        } catch (Exception e) {
            log.error("Error occurred while retrieving departments list", e);
            throw e;
        }
    }

    @Override
    public void delete(Integer id) {
        log.info("Starting to delete department with id: {}", id);
        if (id == null) {
            log.warn("Attempted to delete department with null id");
            throw new IllegalArgumentException("Department ID cannot be null");
        }

        try {
            deptMapper.delete(id);
            log.info("Successfully deleted department with id: {}", id);
        } catch (Exception e) {
            log.error("Error occurred while deleting department with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Dept add(Dept dept) {
        log.info("Starting to add new department: {}", dept != null ? dept.getName() : "null");
        if (dept == null) {
            log.warn("Attempted to add null department");
            throw new IllegalArgumentException("Department cannot be null");
        }

        try {
            LocalDateTime updateTime = LocalDateTime.now();
            LocalDateTime createTime = LocalDateTime.now();
            dept.setUpdateTime(updateTime);
            dept.setCreateTime(createTime);

            deptMapper.add(dept);

            log.info("Successfully added new department: {} with id: {}", dept.getName(), dept.getId());
            return dept;
        } catch (Exception e) {
            log.error("Error occurred while adding department: {}", dept.getName());
            throw e;
        }
    }

    @Override
    public Dept getById(Integer id) {
        log.info("Starting to retrieve department with id: {}", id);
        if (id == null) {
            log.warn("Attempted to get department with null id");
            throw new IllegalArgumentException("Department ID cannot be null");
        }

        try {
            Dept department = deptMapper.getById(id);
            if (department != null) {
                log.info("Successfully retrieved department: {} with id: {}", department.getName(), id);
            } else {
                log.warn("No department found with id: {}", id);
            }
            return department;
        } catch (Exception e) {
            log.error("Error occurred while retrieving department with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Dept update(Dept dept) {
        log.info("Starting to update department with id: {}", dept != null ? dept.getId() : "null");
        if (dept == null) {
            log.warn("Attempted to update null department");
            throw new IllegalArgumentException("Department cannot be null");
        }

        try {
            dept.setUpdateTime(LocalDateTime.now());
            deptMapper.update(dept);
            log.info("Successfully updated department: {} with id: {}", dept.getName(), dept.getId());
            return dept;
        } catch (Exception e) {
            log.error("Error occurred while updating department with id: {}", dept.getId(), e);
            throw e;
        }
    }
}

