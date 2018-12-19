/**
 * 
 */
package org.jretty.spconfig;

import java.util.Properties;
import java.util.Set;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * 配置加载黑科技, See: http://blog.zollty.com/b/index.html (Spring相关)
 * 
 * @author zollty
 * @since 2017-03-11
 */
abstract public class AbstractEnvironmentPostProcessor {
    /** 延迟打印日志 */
    protected static final DeferredLog logger = new DeferredLog();
    
    protected ConfigurableEnvironment env;
    
    /**
     * 将所有配置数据集中在一起
     */
    protected Properties configPropsAll = new Properties();

    protected static PropertyResolver propertyResolver;
    
    public AbstractEnvironmentPostProcessor() {
    }
    
    public AbstractEnvironmentPostProcessor(ConfigurableEnvironment env) {
        this.env = env;
    }
    
    /**
     * 加载本地配置
     */
    abstract protected void loadLocalConfig();
    
    /**
     * 每次都添加到头部，故后添加的优先级会更高
     * （换句话说，最先添加的优先级最低，会被后面的覆盖）。
     */
    protected void add2Head(String name, Properties source) {
        
        MutablePropertySources propertySources = 
                env.getPropertySources();
        propertySources.addFirst(new PropertiesPropertySource(name, source));
        
        logger.info(name + " final config value = " + 
                UTI.Props.sortMapByKey(source, true).toString());
        
        configPropsAll.putAll(source);
    }
    
    protected void add2Tail(String name, Properties source, ConfigurableEnvironment configurableEnvironment) {
        MutablePropertySources propertySources = configurableEnvironment.getPropertySources();
        propertySources.addFirst(new PropertiesPropertySource(name, source));
        
        logger.info(name + " final  config value = " + 
                UTI.Props.sortMapByKey(source, true).toString());
        
        configPropsAll.putAll(source);
    }
    
    public void postProcessEnvironment() {
        
        loadLocalConfig();
        
        loadApolloConfig();

        // 实例化一个默认的PropertyResolver方便后续使用。
        propertyResolver = new PropertySourcesPropertyResolver(env.getPropertySources());
    }
    
    
    protected void loadEnvProfiles(String namesStr) {
        if (namesStr != null && namesStr.trim().length() > 0) {
            // ~ 读取环境相关的配置
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            String[] names = namesStr.split(",");
            logger.info("启动加载配置..." + namesStr);
            for (String name : names) {
                name = name.trim();
                if (name.length() == 0) {
                    continue;
                }
                name = (name.startsWith("classpath:") || name.startsWith("file:")) 
                        ? name + ".properties" : "classpath:" + name + ".properties";
                
                logger.info("配置优先级别-add2Head: 读取本地配置-" + name);
                Properties tmp = UTI.Props.readConf(resolver, name);
                if (tmp != null) {
                    add2Head(name, tmp);
                } else {
                    logger.error("can not find config : " + name);
                }
            }
            logger.info("完成加载配置");
        }
    }

    /**
     * 通过静态方法获取PropertyResolver
     */
    public static PropertyResolver resolver() {
        return propertyResolver;
    }
    
    
    protected void loadApolloConfig() {
        try {
            // 如果有这个类，则自动加载apollo配置
            Class.forName("com.ctrip.framework.apollo.Config");
            doLoadApolloConfig();
        } catch (ClassNotFoundException e) {
            // keep silent..
        }
    }
    
    protected void doLoadApolloConfig() {
        com.ctrip.framework.foundation.internals.provider.DefaultApplicationProvider 
        applicationProvider = 
        new com.ctrip.framework.foundation.internals.provider.DefaultApplicationProvider();
        applicationProvider.initialize();

        String profiles = applicationProvider.getProperty("apollo.profiles.active", null);
        if (profiles != null && profiles.trim().length() > 0) {
            String[] names = profiles.split(",");
            for (String name : names) {
                name = name.trim();
                if (name.length() > 0) {
                    setApolloConfig(name, 
                            com.ctrip.framework.apollo.ConfigService.getConfig(name));
                }
            }
        }
        
        // 最后添加，优先级最高
        setApolloConfig("application", 
                com.ctrip.framework.apollo.ConfigService.getAppConfig());
    }
    
    protected void setApolloConfig(String configFilename, 
            com.ctrip.framework.apollo.Config config) {
        if (config == null) {
            return;
        }
        logger.info("配置优先级别-add2Head:读取apollo配置-" + configFilename);
        Properties remoteProps = new Properties();
        Set<String> propertyNames = config.getPropertyNames();
        for (String propertyName : propertyNames) {
            // 将apollo的属性全部加载到map中
            remoteProps.put(propertyName, config.getProperty(propertyName, null));
        }
        
        // 将apollo的属性加载到environment中
        add2Head("apollo-" + configFilename, remoteProps);
    }
    
}
