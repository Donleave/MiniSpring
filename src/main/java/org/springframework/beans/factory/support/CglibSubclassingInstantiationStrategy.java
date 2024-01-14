package org.springframework.beans.factory.support;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Method;

public class CglibSubclassingInstantiationStrategy implements InstantiationStrategy {

	/**
	 * 使用CGLIB动态生成子类
	 *
	 * @param beanDefinition
	 * @return
	 * @throws BeansException
	 */
	@Override
	public Object instantiate(BeanDefinition beanDefinition) throws BeansException {
		//创建加强器，用来创建动态代理对象
		Enhancer enhancer = new Enhancer();
		//为加强器指定要代理的业务类（即：为下面生成的代理类指定父类）
		enhancer.setSuperclass(beanDefinition.getBeanClass());
		//设置回调
		enhancer.setCallback(new MethodInterceptor() {
			@Override
			//obj:目标对象，即被代理对象
			//method：表示当前调用的方法
			//argsTemp：表示方法的参数
			//proxy：表示生成的代理对象
			public Object intercept(Object obj, Method method, Object[] argsTemp, MethodProxy Proxy) throws Throwable {
				//proxy.invokeSuper调用目标方法
				return Proxy.invokeSuper(obj,argsTemp);
			}
		});
		return enhancer.create();
	}
}
