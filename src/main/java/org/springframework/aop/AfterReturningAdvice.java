package org.springframework.aop;

import java.lang.reflect.Method;

/**
 * 后置增强
 *
 */
public interface AfterReturningAdvice extends AfterAdvice {

    void afterReturning( Object returnValue, Method method, Object[] args,  Object target) throws Throwable;
}
