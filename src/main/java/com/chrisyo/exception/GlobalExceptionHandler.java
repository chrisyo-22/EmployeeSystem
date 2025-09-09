package com.chrisyo.exception;

import com.chrisyo.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@ControllerAdvice
//@ResponseBody These two combine = RestControllerAdvice
@Slf4j
@RestControllerAdvice //Catching exception from Controller Level
public class GlobalExceptionHandler {
    @ExceptionHandler //Specify handle what kind of exception
    public Result handleException(Exception e){
        log.error(e.getMessage());
        return Result.error("Error, please contact admins");
    }

}
