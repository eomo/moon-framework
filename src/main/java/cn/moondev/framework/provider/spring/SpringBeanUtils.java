package cn.moondev.framework.provider.spring;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBeanUtils implements ApplicationContextAware {

    protected static ApplicationContext applicationContext;

    public static <T> T getBean(Class<T> requiredClass) {
        try {
            return applicationContext.getBean(requiredClass);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    public static <T> T getBean(String beanName, Class<T> requiredClass) {
        try {
            return applicationContext.getBean(beanName, requiredClass);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringBeanUtils.applicationContext = applicationContext;
    }

    public static String projectDir() {
        return System.getProperty("user.dir");
    }
}