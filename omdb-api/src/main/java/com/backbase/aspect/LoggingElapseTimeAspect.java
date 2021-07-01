package com.backbase.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@Aspect
@Component
public class LoggingElapseTimeAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingElapseTimeAspect.class);

    @Around("@annotation(requestMapping)")
    public Object logAround(ProceedingJoinPoint joinPoint, RequestMapping requestMapping) throws Throwable {
        long start = System.currentTimeMillis();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        String method = Arrays.toString(requestMapping.method());
        String value = Arrays.toString(requestMapping.value());

        try {
            return joinPoint.proceed();
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            long elapsedTime = System.currentTimeMillis() - start;
            logger.info("Request Controller: " + className + "." + methodName + ", Http Method: " +
                    method + ", Http Path:" + value + " execution time : " + elapsedTime + " ms");
        }
    }
}
