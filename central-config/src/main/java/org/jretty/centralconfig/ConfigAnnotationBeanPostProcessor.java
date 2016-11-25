package org.jretty.centralconfig;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.util.ReflectionUtils;

/**
 * 检查注解：“@ConfigValue”，设置其值为配置中心的值
 * 
 * @author zollty
 * @since 2016-9-10
 */
public class ConfigAnnotationBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    @Resource(name = "configHolder")
    private ConfigHolder configHolder;

    private SimpleTypeConverter converter = new SimpleTypeConverter();

    @Override
    public boolean postProcessAfterInstantiation(final Object bean, String beanName) throws BeansException {
        ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                ConfigValue cfg = field.getAnnotation(ConfigValue.class);
                if (cfg != null) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        throw new IllegalStateException("@ConfigValue 不支持static fields");
                    }
                    // 若未设置@ConfigValue的value，则使用变量域的名称作为键查找配置资源
                    String key = cfg.value().length() == 0 ? field.getName() : cfg.value();
                    String file = cfg.file();
                    Object value;
                    if (file.length() == 0) {
                        value = configHolder.getProperty(key);
                    } else {
                        file = file.indexOf(".") > 0 ? file : file + ".properties";
                        value = configHolder.getPropertyByFile(file, key);
                    }

                    if (value != null) {
                        Object typedValue = converter.convertIfNecessary(value, field.getType());
                        ReflectionUtils.makeAccessible(field);
                        field.set(bean, typedValue);
                    }
                }
            }
        });
        return true;
    }

}