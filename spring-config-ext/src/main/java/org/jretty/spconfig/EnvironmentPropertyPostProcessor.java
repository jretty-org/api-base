package org.jretty.spconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 自定义配置加载
 * <p>1、扩展springboot的配置方式，支持根据这个程序读取配置（如果有重复的，会覆盖springboot的配置）</p>
 * <p>2、引入 Apollo的jar包，会自动识别，自动加载apollo的配置（如果有重复的，会覆盖其他所有配置）。</p>
 * 使用说明：参见README.md
 * 
 * @author zollty
 * @since 2017-11-16
 */
public class EnvironmentPropertyPostProcessor 
    extends AbstractEnvironmentPostProcessor 
        implements EnvironmentPostProcessor, 
            ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment env, 
            SpringApplication application) {
        super.env = env;
        super.postProcessEnvironment();
    }
    
    /**
     * 解决日志输出问题
     */
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        logger.replayTo(getClass());
    }
    
    /**
     * 加载本地配置
     */
    @Override
    protected void loadLocalConfig() {
        // loadGlobalConfig()...
        // ~ 读取环境相关的配置
        String namesStr = getConfigProfiles(env);
        loadEnvProfiles(namesStr);
    }
    
    protected String getConfigProfiles(ConfigurableEnvironment env) {
        String namesStr = null;
        for (String profile : env.getActiveProfiles()) {
            if ("dev".equals(profile)) {
                namesStr = env.getProperty("config.dev");
            } else if ("sit".equals(profile)) {
                namesStr = env.getProperty("config.sit");
            } else if ("uat".equals(profile)) {
                namesStr = env.getProperty("config.uat");
            } else if ("pre".equals(profile)) {
                namesStr = env.getProperty("config.pre");
            } else if ("prod".equals(profile)) {
                namesStr = env.getProperty("config.prod");
            }
        }
        if (namesStr == null) { // DEFAULT_ENV
            namesStr = env.getProperty("config.dev");
        }
        return namesStr;
    }
    
}