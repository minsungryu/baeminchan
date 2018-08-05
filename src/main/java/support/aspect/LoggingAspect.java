package support.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Pointcut("within(codesquad.*.web..*)")
    public void webPoint() {

    }

    @Pointcut("within(codesquad.*.service..*)")
    public void servicePoint() {

    }

    @Around("webPoint() or servicePoint()")
    public Object loggingWebFlow(ProceedingJoinPoint joinPoint) throws Throwable {
        final Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        final String methodName = joinPoint.getSignature().getName();

        if (!isUtilMethod(methodName)) {
            log.debug("{}(): {}", methodName, joinPoint.getArgs());
        }

        Object result = joinPoint.proceed();
        if (!isUtilMethod(methodName)) {
            log.debug("{}(): {}", methodName, result);
        }
        return result;
    }

    private boolean isUtilMethod(String name) {
        return name.startsWith("get") || name.startsWith("set") || name.equals("toString") ||
                name.equals("equals") || name.equals("hashCode");
    }

}
