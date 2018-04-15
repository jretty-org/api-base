package org.jretty.spconfig;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Show application config and status.
 * 
 * @author zollty
 * @since 2017-05-25
 */
@Component
public class ShowConfig implements ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(ShowConfig.class);
    
    private ApplicationContext applicationContext;
    
    @Autowired
    private Environment env;
    
    @PostConstruct
    private void doInit() {
        showBeans(applicationContext);
        showConfig(env);
        showSystemProps();
    }
    
    @Override
    public void setApplicationContext(
            ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    public void setEnv(Environment env) {
        this.env = env;
    }
    
    /** 输出spring 所有bean */
    public void showBeans(ApplicationContext application) {
        if (logger.isDebugEnabled()) {
            StringBuilder sbu = new StringBuilder();
            sbu.append("spring beans total num is ").append(
                    application.getBeanDefinitionCount()).append("\n");

            TreeMap<String, String> tmpMap = new TreeMap<String, String>();
            StringBuilder tmpStr;
            for (String name : application.getBeanDefinitionNames()) {
                Object bean = application.getBean(name);
                String toStr = bean.toString();
                if (toStr.length() > 100) {
                    toStr = toStr.substring(toStr.length() - 100, toStr.length());
                }
                
                tmpStr = new StringBuilder();
                tmpStr.append("spring beans: ").append(name)
                   .append(" - ").append(bean.getClass().getName())
                   .append(" - ").append(toStr).append("\n");
                
                tmpMap.put(bean.getClass().getName(), tmpStr.toString());
            }
            
            for (Map.Entry<String, String> en : tmpMap.entrySet()) {
                sbu.append(en.getValue());
            }

            String info = sbu.toString();
            info = StringUtils.replace(info, "org.springframework.", "spring.");
            logger.info(info);
        }
    }
    
    /** 输出spring配置 */
    public void showConfig(Environment env) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n\r");
        builder.append(" Show spring config ST: -----------------------------------------------------------");
        builder.append("\n\r");
        builder.append("active profiles=").append(Arrays.toString(env.getActiveProfiles())).append("\r\n");
        ConfigurableEnvironment cenv = (ConfigurableEnvironment) env;
        MutablePropertySources srcs = cenv.getPropertySources();
        
        for (PropertySource<?> source : srcs) {
            if (source instanceof EnumerablePropertySource) {
                builder.append("-----------------------PropertySource: " 
                + source.getName() 
                + ", size="
                + ((EnumerablePropertySource<?>) source).getPropertyNames().length+ "\r\n");
                for (String name : ((EnumerablePropertySource<?>) source).getPropertyNames()) {
                    builder.append(name).append("=").append(source.getProperty(name)).append("\r\n");
                }
                builder.append("\r\n");
            }
        }
        builder.append(" Show spring config EN. -----------------------------------------------------------");
        logger.info(builder.toString());
    }
    
    
    /** 输出系统的环境变量和JVM变量 */
    public void showSystemProps() {
        if (logger.isDebugEnabled()) {
            StringBuilder builder = new StringBuilder();
            builder.append("\n\r");
            builder.append(" Show System.getProperties ST: ---------------------------------------------------------");
            builder.append("\n\r");
            Properties props = System.getProperties();
            
            Object[] key = props.keySet().toArray();
            Arrays.sort(key);
            // 排序输出
            for (int i = 0; i < key.length; i++) {
                builder.append(key[i]).append(": ").append(props.get(key[i])).append("\r\n");
            }
            
            builder.append("\n\r");
            builder.append(" Show System.getenv ST: ----------------------------------------------------------------");
            builder.append("\n\r");
            Map<String, String> env = System.getenv();
            
            key = env.keySet().toArray();
            Arrays.sort(key);
            // 排序输出
            for (int i = 0; i < key.length; i++) {
                builder.append(key[i]).append(": ").append(env.get(key[i])).append("\r\n");
            }
            
            builder.append(" Show System.getenv EN. ----------------------------------------------------------------");
            logger.info(builder.toString());
        }
    }
    
}
