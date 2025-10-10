package com.chrisyo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

//@Aspect
//@Component
@Slf4j
public class DemoAspect {

    @Pointcut("execution(* com.chrisyo.service.impl.*.*(..))")
    public void allMethod() {}


    //similar to above, every method that has annotation of Log will be run
    @Pointcut("@annotation(com.chrisyo.aop.Log)")
    public void logAnnotation() {}

    @Around("allMethod()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Before started");

        Object res = joinPoint.proceed();

        log.info("After finished");

        return res;
    }

    @Before("allMethod()")
    public void before(JoinPoint joinpoint) throws Throwable{
        //1. Get a target class name
        String name = joinpoint.getTarget().getClass().getName();
        //2. Get a target method signature
        Signature signature = joinpoint.getSignature();
        //3. Get a target method name
        String methodName = signature.getName();
        //4. Get target method argument (Actually data being passed in)
        Object[] args = joinpoint.getArgs();

        log.info("before tag runs: Before started");
    }

    @After("allMethod()")
    public void after() throws Throwable{
        log.info("after  tag runs: After finished");
    }

    @AfterReturning("allMethod()")
    public void afterReturning() throws Throwable{
        log.info("afterReturning: After returning");
    }

    @AfterThrowing("allMethod()")
    public void afterThrowing() throws Throwable{
        log.info("afterThrowing: After throwing");
    }


}
