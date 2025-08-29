package yun.todo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class TodoServiceLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(TodoServiceLoggingAspect.class);

    @Around("execution(* yun.todo.service..*(..))")
    public Object todoServiceLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.info("[START] {} args={}", methodName, Arrays.toString(args));
        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;

            log.info("[END] {} result={} duration={}ms", methodName, Arrays.toString(args), duration);

            return result;
        }
        catch(Throwable exception) {
            long duration = System.currentTimeMillis() - start;
            log.info("[EXCEPTION] {} exception={} duration={}ms", methodName, exception.toString(), duration);

            throw exception;
        }

    }
}
