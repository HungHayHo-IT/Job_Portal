package com.example.BE.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAndPerformanceAspect {
    //Tìm trong package này và tất cả các package con Áp dụng cho tất cả các lớp, tất cả các phương thức và mọi tham số đầu vào.
    // @Around("@annotation(com.eazybytes.jobportal.aspects.LogAspect)")
    @Around("execution(* com.example.BE..*.*(..))")
    public Object logAndMeasureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();//Lấy tên hàm đang bị đánh chặn.
         Object[] methodArgs = joinPoint.getArgs();//Lấy các giá trị mà người dùng truyền vào hàm đó.
        log.info("➡️ Entering method: {}", methodName);
        log.info("📥 Arguments: {}", Arrays.toString(methodArgs));
        // Proceed with actual business method
        Object result = joinPoint.proceed(); //Nếu bạn không gọi dòng này, hàm gốc sẽ bị chặn đứng hoàn toàn.
        long executionTime = System.currentTimeMillis() - startTime;
        log.info("✅ Method executed successfully: {}", methodName);
        log.info("⏱ Execution time: {} ms", executionTime);
        return result;
    }
}