package org.springframework.aop.framework.adapter;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.BeforeAdvice;
import org.springframework.aop.MethodBeforeAdvice;

/**
 * 前置增强拦截器
 *
 */
public class MethodBeforeAdviceInterceptor implements MethodInterceptor , BeforeAdvice {

	private MethodBeforeAdvice advice;
	public MethodBeforeAdviceInterceptor() {
	}

	public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice) {
		this.advice = advice;
	}

	public void setAdvice(MethodBeforeAdvice advice) {
		this.advice = advice;
	}
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		//在执行被代理方法之前，先执行before advice操作
		this.advice.before(invocation.getMethod(), invocation.getArguments(), invocation.getThis());
		return invocation.proceed();
	}
}
