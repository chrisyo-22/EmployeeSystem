package com.chrisyo.controller;

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

    @PostMapping
    public Result create(@RequestBody Employee employee){
        log.info("Creating new employee: {}", employee);
        empService.create(employee);
        return Result.success();
    }

    @DeleteMapping
    //Another way: delete Integer[] ids, below is via Collection
    public Result delete(@RequestParam List<Integer> ids){
        log.info("Deleting employee with id:{} ", ids);
        empService.delete(ids);
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
