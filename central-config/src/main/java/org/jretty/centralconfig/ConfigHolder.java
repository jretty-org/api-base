/* 
 * Copyright (C) 2016-2017 the CSTOnline Technology Co.
 * Create by ZollTy on 2016-5-16 (zoutianyong@cstonline.com)
 */
package org.jretty.centralconfig;

import java.util.Properties;

/**
 * 定义获取配置的各种接口
 * 
 * @author zollty
 * @since 2016-5-16
 */
public interface ConfigHolder {
    
    /**
     * 通过复制对象引用获取已合并处理的 properties
     * 
     * @param props
     */
    public void loadProperties(Properties props);

    /**
     * 通过key值获取已处理后 properties 单个属性值
     * 
     * @param key
     * @return value
     */
    public String getProperty(String key);
    
    
    /**
     * 通过key值和默认值获取已处理properties单个属性值
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public String getProperty(String key, String defaultValue);
    
    /**
     * 通过复制对象引用获取file的已合并处理的 properties
     * 
     * @param fileId
     * @param props
     */
    public void loadProperties(String fileId, Properties props);

    /**
     * 通过file和key值获取已处理properties单个属性值
     * 
     * @param fileId
     * @param key
     * @return
     */
    public String getPropertyByFile(String fileId, String key);


}
