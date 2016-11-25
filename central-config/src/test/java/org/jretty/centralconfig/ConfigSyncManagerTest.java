/* 
 * Copyright (C) 2016-2017 the CSTOnline Technology Co.
 * Create by ZollTy on 2016-5-16 (zoutianyong@cstonline.com)
 */
package org.jretty.centralconfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jretty.centralconfig.ConfigChangedLogListener;
import org.jretty.centralconfig.ConfigHolder;
import org.jretty.centralconfig.ConfigSyncManager;
import org.jretty.centralconfig.LocalPropsService;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * 
 * @author zollty
 * @since 2016-5-16
 */
public class ConfigSyncManagerTest {

    public static void main(String[] args) throws Exception {

        LocalPropsService localPropsService = new LocalPropsService();

        Map<String, Properties> propsMap = new HashMap<String, Properties>();
        Properties localProp = ConfigSyncManagerTest.getProperties(ConfigConsts.CENTRAL_CONFIG_FILE_PATH);
        if (localProp != null) {
            propsMap.put(ConfigConsts.CENTRAL_CONFIG_ID, localProp);
        }
        localProp = ConfigSyncManagerTest.getProperties(ConfigConsts.GDCP_KAFKA_CONUSMER_CONFIG_PATH);
        if (localProp != null) {
            propsMap.put(ConfigConsts.GDCP_KAFKA_CONUSMER_ID, localProp);
        }

        localPropsService.setLocalProperties(propsMap);

        long start = System.currentTimeMillis();
        ConfigSyncManager manger = new ConfigSyncManager(localPropsService)
                .setChangedListener(new ConfigChangedLogListener());

        ConfigHolder configService = manger.getConfig();

        System.out.println("cost time " + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();

        System.out.println(configService.getPropertyByFile(ConfigConsts.GDCP_KAFKA_CONUSMER_ID, "ds"));
        System.out.println(configService.getProperty("aaaa"));

        manger.refreshConfig();
        System.out.println("refresh cost time " + (System.currentTimeMillis() - start));

        configService = manger.getConfig();
        System.out.println(configService.getPropertyByFile(ConfigConsts.GDCP_KAFKA_CONUSMER_ID, "ds"));
        System.out.println(configService.getProperty("aaaa"));

        System.out.println(configService.getProperty("bbbb"));
    }

    /**
     * 获取Properties资源
     * 
     * @throws IOException
     */
    public static Properties getProperties(String location) throws IOException {
        InputStream io = new PathMatchingResourcePatternResolver().getResource(location).getInputStream();
        Properties tempProps = new Properties();
        tempProps.load(io);
        return tempProps;
    }

}