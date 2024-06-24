//package com.example.datinguserapispring.aspect;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
//import org.springframework.core.ParameterNameDiscoverer;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Method;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//@Aspect
//@Component
//public class LoggingAspect {
//
//    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
//
//    private final ParameterNameDiscoverer paramNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
//
//    @Pointcut("within(com.example.datinguserapispring.service..*)")
//    public void serviceLayer() {
//    }
//
//    @Before("serviceLayer()")
//    public void logMethodEntry(JoinPoint joinPoint) {
//        String methodName = joinPoint.getSignature().getName();
//        Method method = getMethod(joinPoint);
//        String[] paramNames = paramNameDiscoverer.getParameterNames(method);
//        Object[] methodArgs = joinPoint.getArgs();
//
//        Map<String, Object> params = new HashMap<>();
//        for (int i = 0; i < methodArgs.length; i++) {
//            String paramName = paramNames[i];
//            params.put(paramName, methodArgs[i]);
//        }
//
//        logger.info("Entering method: {} with arguments: {}", methodName, params);
//    }
//
//    @AfterReturning(value = "serviceLayer()", returning = "result")
//    public void logMethodExit(JoinPoint joinPoint, Object result) {
//        String methodName = joinPoint.getSignature().getName();
//        logger.info("Exiting method: {}. Return value: {}", methodName, result);
//    }
//
//    private Method getMethod(JoinPoint joinPoint) {
//        Method[] methods = joinPoint.getTarget().getClass().getDeclaredMethods();
//        return Arrays.stream(methods)
//                .filter(method -> method.getName().equals(joinPoint.getSignature().getName()))
//                .findFirst()
//                .orElseThrow(IllegalArgumentException::new);
//    }
//}



