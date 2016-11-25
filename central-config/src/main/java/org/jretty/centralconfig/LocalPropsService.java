/* 
 * Copyright (C) 2016-2017 the CSTOnline Technology Co.
 * Create by ZollTy on 2016-5-16 (zoutianyong@cstonline.com)
 */
package org.jretty.centralconfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 处理和存放本地配置
 * 
 * @author zollty
 * @since 2016-5-16
 */
public class LocalPropsService {

    private Map<String, Properties> localProps = new HashMap<String, Properties>();

    /**
     * 本地配置文件资源
     * 
     * @param paths
     *            如果是 classpath: 开头，表示从 classpath 获取<br>
     *            如果是 / 或者 "X:/" 或者 "X:\"开头，表示磁盘绝对目录<br>
     *            其他开头，表示基于 user.dir 的相对目录
     * @throws Exception
     * @throws IOException
     */
    public void setLocations(Map<String, String> paths) throws IOException {
        Properties tempProps = null;
        // 配置路径迭代器,且根据路径读取配置保存本地配置集合
        for (Map.Entry<String, String> entry : paths.entrySet()) {
            String path = entry.getValue();
            InputStream io = null;
            try {
                tempProps = new Properties();
                if (path.startsWith("classpath:")) {
                    path = path.replaceAll("classpath:", "");
                    io = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
                } else {
                    io = new File(path).canRead() ? new FileInputStream(new File(path)) : null;
                }
                if (io != null) {
                    tempProps.load(io);
                }
                // 加载配置
                localProps.put(entry.getKey(), tempProps);
            } finally {
                if (null != io) {
                    io.close();
                }
            }
        }
    }

    public void setLocation(String fileId, String path) throws IOException {
        Map<String, String> paths = new HashMap<String, String>();
        paths.put(fileId, path);
        setLocations(paths);
    }

    /**
     * 使用 URL 传入本地配置
     * 
     * @param urls
     * @throws IOException
     */
    public void setURLs(Map<String, URL> urls) throws IOException {
        Properties tempProps = null;
        for (Map.Entry<String, URL> entry : urls.entrySet()) {
            URL url = entry.getValue();
            tempProps = new Properties();
            InputStream io = (InputStream) url.openStream();
            tempProps.load(io);
            localProps.put(entry.getKey(), tempProps);
            io.close();
        }
    }

    public void setURL(String fileId, URL url) throws IOException {
        Map<String, URL> urls = new HashMap<String, URL>();
        urls.put(fileId, url);
        setURLs(urls);
    }

    /**
     * 使用 InputStream 传入本地资源
     * 
     * @param ins
     * @throws IOException
     */
    public void setInputStreams(Map<String, InputStream> ins) throws IOException {
        Properties tempProps = null;
        for (Map.Entry<String, InputStream> entry : ins.entrySet()) {
            InputStream io = entry.getValue();
            tempProps = new Properties();
            tempProps.load(io);
            localProps.put(entry.getKey(), tempProps);
        }
    }

    public void setInputStream(String fileId, InputStream in) throws IOException {
        Map<String, InputStream> ins = new HashMap<String, InputStream>();
        ins.put(fileId, in);
        setInputStreams(ins);
    }

    /**
     * 直接使用 properties 传入本地配置
     * 
     * @param props
     */
    public void setLocalProperties(Map<String, Properties> props) {
        localProps = props;
    }

    public Map<String, Properties> getLocalProps() {
        return localProps;
    }

    public Properties getLocalProps(String fileId) {
        return localProps.get(fileId);
    }

    void ensureInitial() {
        if (localProps.isEmpty()) {
            throw new IllegalStateException(
                    "Pls make sure LocalPropsService has loaded the config. One way is using LocalPropsService#setLocations().");
        }
    }
    
    public String getNotEmptyValue(String key) {
        String val = null;
        for (Map.Entry<String, Properties> entry : localProps.entrySet()) {
            Properties prop = entry.getValue();
            if (prop.containsKey(key)) {
                val = prop.getProperty(key);
                break;
            }
        }
        if (val != null) {
            val = val.trim();
            return val.length() != 0 ? val : null;
        }
        return null;
    }

    String getStringDefValue(String key, String curVal, String defVal) {
        String val = "";
        // 判断当前值,若空值则读取本地配置属性值
        // 若本地属性值空值,则默认属性值
        if ("".equals(trimVal(curVal))) {

            for (Map.Entry<String, Properties> entry : localProps.entrySet()) {
                Properties prop = entry.getValue();
                if (prop.containsKey(key)) {
                    val = prop.getProperty(key);
                    break;
                }
            }

            if ("".equals(trimVal(val))) {
                val = defVal;
            }
        } else {
            val = curVal;
        }
        return trimVal(val);
    }

    protected String trimVal(String val) {
        return val == null ? "" : val.trim();
    }

}