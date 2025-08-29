package yun.todo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class TodoServiceLoggingAspect {

    @Around("execution(* yun.todo.service..*(..))")
    public Object todoServiceLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.info("[Start] {} args={}", methodName, Arrays.toString(args));
        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;

            log.info("[End] {} result={} duration={}ms", methodName, Arrays.toString(args), duration);

            return result;
        }
        catch(Throwable exception) {
            long duration = System.currentTimeMillis() - start;
            log.error("[Exception] {} exception={} duration={}ms", methodName, exception.toString(), duration);

            throw exception;
        }

    }
}
