package com.chrisyo.controller;


import com.chrisyo.aop.Log;
import com.chrisyo.entity.Dept;
import com.chrisyo.entity.Result;
import com.chrisyo.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/depts")
@RestController // = Controller + ResponseBody
//@Controller -> Generate Bean object, it's not equivalent to Component because Component cannot accept request
//@ResponseBody //use: convert our return into json object
public class DeptController {
    @Autowired //In the IOC container, find a bean object and give the value to deptService
    private DeptService deptService;


    /**
     * Department Query
     *
     * @return List of Departments
     */
    //@RequestMapping(path="/depts", method =RequestMethod.GET)
    @GetMapping //Equivalent to above
    public Result getAll() {
        //1. Invoke service and get data
        List<Dept> depts = deptService.list();

        //3. Return data as JSON
        return Result.success(depts); // directly return (will be converted to JSON by ResponseBody)
    }

    /**
     * Delete Department
     */
    @Log
    @DeleteMapping
    //public Result delete(@RequestParam("id") Integer deptId) Mapping between id to deptId
    public Result delete(Integer id) {
        deptService.delete(id);
        return Result.success();
    }

    /**
     * Create a department
     *
     * @return created department
     */
    @Log
    @PostMapping
    public Result create(@RequestBody Dept dept) {
        Dept new_dept = null;
        try {
            new_dept = deptService.add(dept);
        } catch (Exception e) {
            return Result.error("Failed to create new department: " + dept.getName());

        }
        return Result.success(new_dept);
    }

    /**
     * Query a department
     */
    @GetMapping("/{id}")
    public Result get(@PathVariable Integer id) {
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }

    /**
     * Modify a department
     */
    @Log
    @PutMapping
    public Result update(@RequestBody Dept dept) {
        Dept updated = deptService.update(dept);
        return Result.success(updated);
    }

}
