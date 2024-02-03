package org.springframework.aop.framework;

import org.springframework.aop.AdvisedSupport;

import java.lang.reflect.Method;
import java.util.List;

/**
 *用于创建 Advisor 链（切面链）
 */
public interface AdvisorChainFactory {


	List<Object> getInterceptorsAndDynamicInterceptionAdvice(AdvisedSupport config, Method method, Class<?> targetClass);

}
