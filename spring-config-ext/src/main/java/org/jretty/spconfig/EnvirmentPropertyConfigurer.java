package org.jretty.spconfig;

import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.ConfigurablePropertyResolver;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.io.Resource;
import org.springframework.util.SystemPropertyUtils;

/**
 * 自定义配置加载
 * <p>1、扩展spring的PropertyPlaceholderConfigurer配置方式，支持根据环境变量选择加载配置</p>
 * <p>2、引入 Apollo的jar包，会自动识别，自动加载apollo的配置（如果有重复的，会覆盖其他所有配置）。</p>
 * 使用说明：参见README.md
 * 
 * @author zollty
 * @since 2017-10-11
 */
public class EnvirmentPropertyConfigurer extends PropertySourcesPlaceholderConfigurer {

    protected static final Log LOG = LogFactory.getLog(EnvirmentPropertyConfigurer.class);
    private static final String DEFAULT_ENV = "dev";
    
    /** 全局配置 */
    protected Resource[] globalLocations;
    /** 环境相关配置 */
    protected Map<String, String> environmentLocations;
    
    /**
     * 将所有配置数据集中在一起
     */
    protected Properties configPropsAll;
    
    private static PropertyResolver propertyResolver;
    
    /**
     * 这个方法执行的优先级非常高，在Spring刚启动的时候就会调用
     */
    @Override
    public void setEnvironment(Environment environment) {
        AbstractEnvironmentPostProcessor jep = new AbstractEnvironmentPostProcessor(
                (ConfigurableEnvironment) environment) {
            @Override
            protected void loadLocalConfig() {
                if (globalLocations != null) {
                    for (int i = 0; i < globalLocations.length; i++) {
                        Properties tmp = UT.Props.getProperties(globalLocations[i]);
                        add2Head(globalLocations[i].getFilename(), tmp);
                    }
                }
                
                // ~ 读取环境相关的配置
                String namesStr = getConfigProfiles();
                loadEnvProfiles(namesStr);
            }
        };
        jep.postProcessEnvironment();
        AbstractEnvironmentPostProcessor.logger.replayTo(getClass());
        
        propertyResolver = AbstractEnvironmentPostProcessor.resolver();
        
        configPropsAll = jep.configPropsAll;
        
        super.setEnvironment(environment);
    }
    
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,
            final ConfigurablePropertyResolver propertyResolver) throws BeansException {
        // 保存PropertyResolver方便后续使用。
        EnvirmentPropertyConfigurer.propertyResolver = propertyResolver;
        super.processProperties(beanFactoryToProcess, propertyResolver);
    }
    
    /**
     * 获取PropertyResolver
     */
    public static PropertyResolver resolver() {
        return propertyResolver;
    }
    
    protected String getConfigProfiles() {
        if (environmentLocations != null && !environmentLocations.isEmpty()) {
            // ~ 读取环境相关的配置
            String profile = SystemPropertyUtils.resolvePlaceholders("${spring.profiles.active}", true);
            String namesStr = null;
            if (!profile.startsWith("${")) {
                namesStr = environmentLocations.get(profile);
                if (namesStr == null) {
                    throw new IllegalStateException("加载配置异常，不存在该profile的配置。profile=" + profile);
                }
            } else {
                namesStr = environmentLocations.get(DEFAULT_ENV);
            }
            return namesStr;
        }
        return null;
    }
    
    
    public void setGlobalLocation(Resource globalLocation) {
        this.globalLocations = new Resource[] { globalLocation };
        setGlobalLocations(globalLocation);
    }

    public void setGlobalLocations(Resource... globalLocations) {
        this.globalLocations = globalLocations;
        super.setLocations(globalLocations);
    }
    
    public void setEnvironmentLocations(Map<String, String> environmentLocations) {
        this.environmentLocations = environmentLocations;
    }

}