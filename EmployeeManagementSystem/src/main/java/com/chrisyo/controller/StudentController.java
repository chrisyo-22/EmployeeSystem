package com.chrisyo.controller;

import com.chrisyo.aop.Log;
import com.chrisyo.entity.PageBean;
import com.chrisyo.entity.Result;
import com.chrisyo.entity.Student;
import com.chrisyo.entity.StudentQueryParams;
import com.chrisyo.service.StudentService;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@ControllerAdvice
@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    StudentService studentService;


    @GetMapping
    public Result page(StudentQueryParams stuQueryParam) {
        log.info("Query students by page: {}", stuQueryParam.toString().replace("\n", ""));
        PageBean res = studentService.page(stuQueryParam);
        return Result.success(res);
    }

    @Log
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable List<Integer> ids) {
        log.info("Deleting student with id: {}", ids);
        studentService.delete(ids);
        return Result.success("successfully deleted student with id: " + ids);
    }

    @Log
    @PostMapping
    public Result add(@RequestBody Student stuObj) {
        log.info("Adding student: {}", stuObj.toString().replace("\n", ""));
        studentService.add(stuObj);
        return Result.success("successfully added student: " + stuObj.toString());
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("Querying Student with id: {}", id);
        Student queryStudent = studentService.getById(id);
        return Result.success(queryStudent);
    }

    @Log
    @PutMapping
    public Result put(@RequestBody Student stuObj) {
        log.info("updating student: {}", stuObj.toString().replace("\n", ""));
        studentService.update(stuObj);
        return Result.success("successfully updated student: " + stuObj.toString());
    }

    @Log
    @PutMapping("/violation/{id}/{score}")
    public Result violationRecord(@PathVariable Integer id, @PathVariable Integer score) {
        studentService.updateViolation(id, score);
        return Result.success("successfully updated student: " + id + " with score: " + score);
    }


}
