package org.jretty.spconfig;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 通过Spring的ApplicationContext，以静态方法的方式获取spring 的bean实例
 * 
 * @author zollty
 * @since 2016-5-23
 */
@Component
public class SpringAppContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringAppContext.applicationContext = context;
    }

    /**
     * 根据bean name 从spring 容器里面得到bean
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        if (applicationContext == null) {
            return null;
        }
        try {
            return (T) applicationContext.getBean(beanName);
        } catch (Exception e) {
            // ignore...
            return null;
        }
    }

    /**
     * 根据bean CLASS 从spring 容器里面得到bean
     */
    public static <T> T getBean(Class<T> claz) {
        if (applicationContext == null) {
            return null;
        }
        try {
            return (T) applicationContext.getBean(claz);
        } catch (Exception e) {
            // ignore...
            return null;
        }
    }

}