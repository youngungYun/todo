package yun.todo.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class TodoControllerLoggingAspect {

    @Around("execution(* yun.todo.controller..*(..))")
    public Object todoControllerLogging(ProceedingJoinPoint joinPoint) throws Throwable {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            HttpServletRequest request = servletRequestAttributes.getRequest();

            String httpMethod = request.getMethod();
            String requestUrl = request.getRequestURL().toString();

            log.info("Request: {} {} ", httpMethod, requestUrl);
        }

        try {
            return joinPoint.proceed();
        }
        catch(Throwable exception) {
            throw exception;
        }
    }
}
