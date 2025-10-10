package com.chrisyo.controller;

import com.chrisyo.aop.Log;
import com.chrisyo.entity.Class;
import com.chrisyo.entity.PageBean;
import com.chrisyo.entity.Result;
import com.chrisyo.service.ClassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.chrisyo.entity.ClassQueryParam;

import java.util.List;

@Slf4j
@RequestMapping("/class")
@RestController
@ControllerAdvice
public class ClassController {

    @Autowired
    private ClassService classService;

    @GetMapping
    public Result page(ClassQueryParam classQueryParam) {
        log.info("query class by page: {}", classQueryParam.toString().replace("\n", ""));
        PageBean res = classService.page(classQueryParam);
        return Result.success(res);
    }

    @Log
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("delete class with id: {}", id);
        classService.delete(id);
        return Result.success();
    }

    @Log
    @PostMapping
    public Result add(@RequestBody Class classObj) {
        log.info("add class: {}", classObj.toString().replace("\n", ""));
        classService.add(classObj);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getClassById(@PathVariable Integer id) {
        log.info("query class by id: {}", id);
        Class res = classService.getClassById(id);
        return Result.success(res);
    }

    @Log
    @PutMapping
    public Result update(@RequestBody Class classObj) {
        log.info("updating class: {}", classObj.toString().replace("\n", ""));
        classService.update(classObj);
        return Result.success();
    }

    @GetMapping("/list")
    public Result getAll() {
        log.info("Query all classes");
        List<Class> res = classService.getAll();
        return Result.success(res);
    }

}
