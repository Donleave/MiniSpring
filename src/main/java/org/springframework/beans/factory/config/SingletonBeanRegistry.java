package org.springframework.beans.factory.config;

/**
 * 单例注册表
 *
 * @author Donleave
 * @data 2024/1/13
 */
public interface SingletonBeanRegistry {

	Object getSingleton(String beanName);

	void addSingleton(String beanName, Object singletonObject);
}