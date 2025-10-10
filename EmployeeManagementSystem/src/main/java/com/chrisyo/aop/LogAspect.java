package com.chrisyo.aop;

import com.chrisyo.entity.OperateLog;
import com.chrisyo.mapper.OperateLogMapper;
import com.chrisyo.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Component
@Aspect //切面类
public class LogAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Around("@annotation(com.chrisyo.aop.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        //- Current employee ID
        //get jwt and verify if it's valid and get id
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJwt(jwt);
        Integer operateEmpId = (Integer) claims.get("id");

        //Operation time
        LocalDateTime operateTime = LocalDateTime.now();

        //Operating Class Name
        String className = joinPoint.getTarget().getClass().getName();

        //Operating Method
        String methodName = joinPoint.getSignature().getName();

        //Operating Arguments
        Object[] args = joinPoint.getArgs();
        String methodParams = Arrays.toString(args);

        long begin = System.currentTimeMillis();
        //Invoke original method(Within the proxy)
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();

        //Return whatever result from the original function
        String returnValue = result.toString();

        //Calculate performance(time)
        Long costTime = end - begin;

        //Log the operation into the database
        OperateLog operateLog = new OperateLog(null,operateEmpId,operateTime,className,methodName,methodParams,returnValue,costTime);
        operateLogMapper.insert(operateLog);

        log.info("AOP Logging Operation: {}" , operateLog);

        return result;
    }

}
