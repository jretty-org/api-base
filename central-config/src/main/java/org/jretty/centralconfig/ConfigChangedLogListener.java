/* 
 * Copyright (C) 2016-2017 the CSTOnline Technology Co.
 * Create by ZollTy on 2016-5-17 (zoutianyong@cstonline.com)
 */
package org.jretty.centralconfig;

import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ConfigChangedListener的简单实现，当配置发生变更时记录日志
 * 
 * @author zollty
 * @since 2016-5-17
 */
public class ConfigChangedLogListener implements ConfigChangedListener {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigChangedLogListener.class);

    @Override
    public void onChanged(String configFilename, Properties curProps, 
            Properties oldProps, Set<String> addedKeys,
            Set<String> changedKeys, Set<String> removedKeys) {

        StringBuilder sbu = new StringBuilder();
        sbu.append("configFilename: ").append(configFilename).append("\n\r");
        sbu.append("addedKeys: ");
        for (String key : addedKeys) {
            sbu.append(key).append(", ");
        }
        sbu.append("\n\rchangedKeys: ");
        for (String key : changedKeys) {
            sbu.append(key).append(", ");
        }
        sbu.append("\n\rremovedKeys: ");
        for (String key : removedKeys) {
            sbu.append(key).append(", ");
        }
        LOG.info(sbu.toString());
    }

}