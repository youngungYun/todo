package yun.todo.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
public class TodoControllerLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(TodoControllerLoggingAspect.class);

    @Around("execution(* yun.todo.controller..*(..))")
    public Object todoControllerLogging(ProceedingJoinPoint joinPoint) throws Throwable {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            HttpServletRequest request = servletRequestAttributes.getRequest();

            String httpMethod = request.getMethod();
            String requestUrl = request.getRequestURL().toString();

            log.info("Request: {} {} ", httpMethod, requestUrl);
        }

        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.info("[START] {} args={}", methodName, Arrays.toString(args));

        try {
            return joinPoint.proceed();
        }
        catch(Throwable exception) {
            throw exception;
        }
    }
}
