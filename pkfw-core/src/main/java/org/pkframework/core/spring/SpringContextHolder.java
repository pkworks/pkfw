package org.pkframework.core.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContextHolder {

	private static ApplicationContext applicationContext;

	public static void init(String[] configLocations) {
		applicationContext = new ClassPathXmlApplicationContext(configLocations);
	}

	public static <T> T getBean(Class<T> requiredType) {
		return applicationContext.getBean(requiredType);
	}

}
