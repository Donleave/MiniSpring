package org.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;

//主要负责实现 Bean 的自动装配功能
//抽象类中可以不包含抽象方法，也可以包含抽象方法，但含有抽象方法的类一定 是 抽象类。
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

	private InstantiationStrategy instantiationStrategy=new SimpleInstantiationStrategy();

	@Override
	protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
		return doCreateBean(beanName, beanDefinition);
	}

	protected Object doCreateBean(String beanName, BeanDefinition beanDefinition) {

		Object bean = null;
		try {
			bean = createBeanInstance(beanDefinition);
			//为bean填充属性
			applyPropertyValues(beanName, bean, beanDefinition);
		} catch (Exception e) {
			throw new BeansException("Instantiation of bean failed", e);
		}

		addSingleton(beanName, bean);
		return bean;
	}
	/**
	 * 为bean填充属性
	 *
	 * @param bean
	 * @param beanDefinition
	 */
	protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
		try {
			for (PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValues()) {
				String name = propertyValue.getName();
				Object value = propertyValue.getValue();

				//通过反射设置属性
				//BeanUtil.setFieldValue 方法使用反射机制，根据属性名在 Bean 实例中找到相应的字段，并将对应的值设置给字段
				BeanUtil.setFieldValue(bean, name, value);
			}
		} catch (Exception ex) {
			throw new BeansException("Error setting property values for bean: " + beanName, ex);
		}
	}
	protected Object createBeanInstance(BeanDefinition beanDefinition) {
		return getInstantiationStrategy().instantiate(beanDefinition);
	}

	public InstantiationStrategy getInstantiationStrategy() {
		return instantiationStrategy;
	}

	public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
		this.instantiationStrategy = instantiationStrategy;
	}
}
