package com.chrisyo.controller;

import com.chrisyo.entity.Employee;
import com.chrisyo.entity.LoginInfo;
import com.chrisyo.entity.Result;
import com.chrisyo.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    EmpService empService;

    /**
     * User login
     * @param emp employee login info
     * @return user login info
     */
    @PostMapping("/login")
    public Result login(@RequestBody Employee emp){
        log.info("Login request: {}", emp);
        LoginInfo loginInfo = empService.login(emp);
        if(loginInfo == null){
            return Result.error("Login failed, Either username or password is incorrect.");
        }
        return Result.success(loginInfo);
    }
}
