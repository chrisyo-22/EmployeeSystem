package com.chrisyo.service.impl;

import com.chrisyo.entity.*;
import com.chrisyo.mapper.EmpMapper;
import com.chrisyo.service.EmpService;
import com.chrisyo.utils.AwsS3Utils;
import com.chrisyo.utils.JwtUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.chrisyo.mapper.EmpExprMapper;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;
    @Autowired
    private AwsS3Utils awsS3Utils;

//    /**
//     * Query data by page
//     * @param page current page # 1,2,3,...
//     * @param pageSize # of data display per page
//     * @return total rows of data and data rows
//     */
//    @Override
//    public PageBean page(Integer page, Integer pageSize) {
//        //1. Invoke mapper to get total rows of data
//        Long total = empMapper.count();
//
//        //2. Invoke mapper to get data
//        Integer start_index = (page - 1) * pageSize;
//
//        List<Employee> emp_list = empMapper.page(start_index, pageSize);
//
//        //3. Encapsulate the data and return
//        return new PageBean(total, emp_list);
//    }


    @Override
    public PageBean page(EmpQueryParam empQueryParam) {
        //Set page params
        PageHelper.startPage(empQueryParam.getPage(), empQueryParam.getPageSize());
        List<Employee> empList = empMapper.list(empQueryParam);

        //Retrieve a pre-signed file link from S3
        empList.forEach(emp -> {
            String presignUrl = awsS3Utils.createPresignedGetUrl(emp.getImage());
            emp.setImage(presignUrl);
        });

        Page<Employee> p = (Page<Employee>) empList; //Type casting the Page<Employee>object, it inherent ArrayList
        return new PageBean(p.getTotal(), p.getResult()); // p === p.getResult()
    }

    @Transactional
    @Override
    public void create(Employee employee) {
        //1. invoke Mapper to save employee info to emp table
        //1.1 fill missing columns
        employee.setPassword("123456");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setCreateTime(LocalDateTime.now());

        //1.2 invoke Mapper
        empMapper.insert(employee);
        Integer employeeId = employee.getId(); //auto inserted by Option Annotation
        log.info("id={}", employeeId);

        //2. invoke Mapper to save employee's job experience to experience table
        List<EmpExpr> exprList = employee.getExprList();
        //2.1 inject employee id into experience
        if (!CollectionUtils.isEmpty(exprList)) {
            exprList.forEach(expr -> {
                expr.setEmpId(employeeId);
            });
            //2.2 invoke mapper
            empExprMapper.insertBatch(exprList);
        }


    }

    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        //1. Delete employee from emp table
        empMapper.deleteBatch(ids);

        //2. delete employee's experience(expr table)
        empExprMapper.deleteBatch(ids);
    }

    @Override
    public Employee getEmpById(Integer id) {
        return empMapper.getEmpById(id);
    }

    //    @Override
    public Employee getEmpByIdV2(Integer id) {
        //1. query employee
        Employee employee = empMapper.getEmpByIdWithoutExpr(id);
        //2. query employee experiences
        List<EmpExpr> expr = empMapper.getExpExprById(id);
        //3. combine and return
        employee.setExprList(expr);
        return employee;
    }

    @Transactional
    @Override
    public void update(Employee employee) {

        //1. Update employee basic info
        //1.1 Update the time of modification
        employee.setUpdateTime(LocalDateTime.now());
        empMapper.update(employee);

        //2. Delete the expr from the emp-expr table and add new ones (Delete first then add)
        //This is because the frontend supports modification, deletion, and addition.
        //It would be much simpler to just overwrite everything
        empExprMapper.deleteByEmpId(employee.getId());
        List<EmpExpr> exprList = employee.getExprList();
        //If not empty
        if (!CollectionUtils.isEmpty(exprList)) {
            exprList.forEach(expr -> {
                expr.setEmpId(employee.getId());
            });
            empExprMapper.insertBatch(exprList);
        }
    }

    @Override
    public LoginInfo login(Employee emp) {
        //1. Invoke Mapper to check if this employee(user) exists
        Employee empInfo = empMapper.selectUsernameAndPwd(emp);

        //2. check if the password is correct, If successfully queried, construct the LoginInfo object and return
        if (empInfo != null) {
            //2.1 generate JWT
            Claims claims = Jwts.claims()
                    .add("id", empInfo.getId())
                    .add("username", empInfo.getUsername())
                    .build();
            String jwt = JwtUtils.createJwtToken(claims);
            return new LoginInfo(empInfo.getId(), empInfo.getUsername(), empInfo.getName(), jwt);
        }
        //3. NO such user
        return null;
    }
}
