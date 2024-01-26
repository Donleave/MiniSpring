package org.springframework.aop;


public interface ClassFilter {

	/**
	 * 用于判断给定的类是否满足切点的条件。
	 * @param clazz
	 * @return
	 */
	boolean matches(Class<?> clazz);
}
