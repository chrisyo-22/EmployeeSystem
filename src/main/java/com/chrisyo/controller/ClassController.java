package com.chrisyo.controller;

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
public class ClassController {

    @Autowired
    private ClassService classService;

    @GetMapping
    public Result page(ClassQueryParam classQueryParam) {
        log.info("query class by page: {}", classQueryParam.toString().replace("\n", ""));
        PageBean res = classService.page(classQueryParam);
        return Result.success(res);
    }


    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids) {
        log.info("delete class with ids: {}", ids);
        classService.delete(ids);
        return Result.success();
    }

    @PostMapping
    public Result add(@RequestBody Class classObj) {
        log.info("add class: {}", classObj.toString().replace("\n", ""));
        classService.add(classObj);
        return Result.success();
    }

    @GetMapping
    public Result getClassById(Integer id) {
        log.info("query class by id: {}", id);
        Class res = classService.getClassById(id);
        return Result.success(res);
    }

    @PutMapping
    public Result update(Class classObj) {
        log.info("updating class: {}", classObj.toString().replace("\n", ""));
        classService.update(classObj);
        return Result.success();
    }

    @GetMapping
    public Result getAll() {
        log.info("Query all classes");
        List<Class> res = classService.getAll();
        return Result.success(res);
    }

}
