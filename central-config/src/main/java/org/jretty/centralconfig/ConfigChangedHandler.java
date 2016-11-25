/* 
 * Copyright (C) 2016-2017 the CSTOnline Technology Co.
 * Create by ZollTy on 2016-5-16 (zoutianyong@cstonline.com)
 */
package org.jretty.centralconfig;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * 检测配置是否发生变更，若发生变更则调用外部的ConfigChangedListener去处理
 * 
 * @author zollty
 * @since 2016-5-16
 */
public class ConfigChangedHandler {

    /**
     * hold old Config Properties
     */
    private Properties oldConfigProps = new Properties();

    /**
     * 远程配置文件名称
     */
    private final String configFilename;

    /**
     * see {@link ConfigChangedListener}
     */
    private final ConfigChangedListener changedListener;

    public ConfigChangedHandler(String configFilename, ConfigChangedListener changedListener) {
        super();
        this.configFilename = configFilename;
        this.changedListener = changedListener;
    }

    public void configChanged(Properties configProps) {
        if (null == changedListener) {
            return;
        }
        boolean callback = false;
        Set<String> changedKeys = new HashSet<String>();
        Set<String> removedKeys = new HashSet<String>();
        Set<String> addedKeys = new HashSet<String>();
        // 旧比配与当前配置比较,若旧配置中不包含当前配置Key值,
        // 则说明当前配置中新增该Key配置属性
        // 若旧配置包含当前配置Key值,比较两个配置属性值,
        // 若值相等则不做处理,否则说明修改过该Key配置属性值
        Iterator<?> iterator = configProps.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if (oldConfigProps.containsKey(key)) {
                String curVal = trimVal(configProps.getProperty(key));
                String oldVal = trimVal(oldConfigProps.getProperty(key));
                if (curVal.equals(oldVal)) {
                    continue;
                } else {
                    callback = true;
                    changedKeys.add(key);
                }
            } else {
                callback = true;
                addedKeys.add(key);
            }
        }
        // 当前配置与旧配置比较,若当前配置中不包含旧配置Key值,
        // 则说明当前配置中已删除该key配置属性
        iterator = oldConfigProps.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if (configProps.containsKey(key)) {
                continue;
            } else {
                callback = true;
                removedKeys.add(key);
            }
        }
        // 若配置存在新增,删除,修改等操作,则执行回调函数
        if (callback) {
            changedListener.onChanged(configFilename, configProps, 
                    oldConfigProps, addedKeys, changedKeys, removedKeys);
        }

        oldConfigProps.clear();
        oldConfigProps.putAll(configProps);
    }

    private String trimVal(String val) {
        return val == null ? "" : val.trim();
    }
}