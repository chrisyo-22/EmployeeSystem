package com.chrisyo.controller;

import com.chrisyo.aop.Log;
import com.chrisyo.entity.EmpQueryParam;
import com.chrisyo.entity.Employee;
import com.chrisyo.entity.PageBean;
import com.chrisyo.entity.Result;
import com.chrisyo.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {

    @Autowired
    private EmpService empService;

    @GetMapping
    public Result page(EmpQueryParam empQueryParam) {
        log.info("Queried By page: page:{}, pageSize:{}, name: {}, gender:{}, begin:{} --- end:{}", empQueryParam.getPage(), empQueryParam.getPageSize(), empQueryParam.getName(), empQueryParam.getGender(), empQueryParam.getBegin(), empQueryParam.getEnd());
        PageBean result = empService.page(empQueryParam);
        return Result.success(result);
    }

    @Log
    @PostMapping
    public Result create(@RequestBody Employee employee) {
        //log.info("Creating new employee: {}", employee);
        empService.create(employee);
        return Result.success();
    }
    @Log
    @DeleteMapping
    //Another way: delete Integer[] ids, below is via Collection
    public Result delete(@RequestParam List<Integer> ids) {
        //log.info("Deleting employee with id:{} ", ids);
        empService.delete(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("Getting employee with id:{} ", id);
        Employee emp = empService.getEmpById(id);
        return Result.success(emp);
    }

    @Log
    @PutMapping
    public Result update(@RequestBody Employee employee) {
        //log.info("Updating employee: {}", employee);
        empService.update(employee);
        return Result.success();
    }


    //    public Result page(@RequestParam(defaultValue = "1")
//                       Integer page,
//                       @RequestParam(defaultValue = "10")
//                       Integer pageSize,
//                       String name,
//                       Integer gender,
//                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
//                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {


}
