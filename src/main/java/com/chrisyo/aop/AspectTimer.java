package com.chrisyo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//@Aspect // declaration of this is an Aspect type
//@Component
@Slf4j
public class AspectTimer {

    /**
     * Summarize the time to execute a function
     * Around:
     *      (..) means all parameter types
     *      .* means all file, all methods
     * @return time
     */
    @Around("execution(* com.chrisyo.service.impl.*.*(..))")
    public Object recordTime(ProceedingJoinPoint joinPoint) throws Throwable{
        //1. get begin-time execution
        long begin = System.currentTimeMillis();

        //2. Execute native/original function
        Object result = joinPoint.proceed();

        //3. get end-time execution
        long end = System.currentTimeMillis();

        //4. Calculate time used
        log.info("Time elapsed: {} ms", end - begin);
        //5. Return response object
        return result;
    }
}
