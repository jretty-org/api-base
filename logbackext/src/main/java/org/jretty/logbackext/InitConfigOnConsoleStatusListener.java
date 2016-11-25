package org.jretty.logbackext;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jretty.centralconfig.ConfigChangedLogListener;
import org.jretty.centralconfig.ConfigHolder;
import org.jretty.centralconfig.ConfigSyncManager;
import org.jretty.centralconfig.LocalPropsService;
import org.jretty.util.ResourceUtils;

import ch.qos.logback.core.status.OnConsoleStatusListener;

/**
 * 从统一配置中心读取logback配置
 * 
 * @author zollty
 * @since 2016年8月26日
 */
public class InitConfigOnConsoleStatusListener extends OnConsoleStatusListener {
    
    /**
     * 配置中心文件路径
     */
    private static final String CENTRAL_CONFIG_FILE_PATH = "classpath:zkconf4log/central-config.properties";
    private static final String CENTRAL_CONFIG_ID = "central-config.properties";

    private boolean init;

    @Override
    protected PrintStream getPrintStream() {
        if (!init) {
            super.getPrintStream().println("Init log val....................................................");
            initConfigService();
            init = true;
        }
        return super.getPrintStream();
    }

    /**
     * 初始化配置
     * 
     * @throws IOException
     */
    private void initConfigService() {
        try {
            ConfigHolder configHolder = getConfigService();
            setPro(configHolder);
        } catch (IOException e) {
            super.getPrintStream().println(e.getMessage());
        }
    }
    
    private void setPro(ConfigHolder configHolder) {
        Properties props = new Properties();
        configHolder.loadProperties(props);
        for (Object ok : props.keySet()) {
            String key = ok.toString();
            String val = props.getProperty(key);
            System.setProperty(key, val);
            super.getPrintStream().println(key + ": " + val);
        }
    }

    private ConfigHolder getConfigService() throws IOException {
        LocalPropsService localPropsService = new LocalPropsService();

        Map<String, Properties> propsMap = new HashMap<String, Properties>();

        Properties localProp = getProperties(CENTRAL_CONFIG_FILE_PATH);
        if (localProp != null) {
            propsMap.put(CENTRAL_CONFIG_ID, localProp);
        }

        localPropsService.setLocalProperties(propsMap);

        ConfigSyncManager manger = new ConfigSyncManager(localPropsService)
                .setChangedListener(new ConfigChangedLogListener());

        return manger.getConfig();
    }

    /**
     * 获取Properties资源
     */
    private static Properties getProperties(String location) throws IOException {

        return ResourceUtils.getProperties(ResourceUtils.getClassPathResource(location).getInputStream());
    }

}