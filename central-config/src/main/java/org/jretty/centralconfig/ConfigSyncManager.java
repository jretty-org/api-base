/* 
 * Copyright (C) 2016-2017 the CSTOnline Technology Co.
 * Create by ZollTy on 2016-5-16 (zoutianyong@cstonline.com)
 */
package org.jretty.centralconfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 集中的配置管理（借助zookeeper管理分布式集群的统一配置）<br>
 * 
 * 方案一：每次获取ConfigSyncManager时就刷新一次配置 <br>
 * 方案二：第一次获取ConfigSyncManager时才刷新一次配置，以后使用上一次刷新后的配置<br>
 * 
 * 采用方案二，每次使用new新对象
 * 
 * @author zollty
 * @since 2016-5-16
 */
public class ConfigSyncManager extends AbstractConfigSyncManager {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigSyncManager.class);
    /**
     * zookeeper连接的超时时间
     */
    protected static final int connectionTimeoutMs = 10000;

    // 根据路径层级读取各层级配置
    protected List<String> pathLevelList = new ArrayList<String>();

    private String randomPath = UUID.randomUUID().toString();
    protected Map<String, ConfigChangedHandler> configChangedHandlerMap = 
    		new HashMap<String, ConfigChangedHandler>();
    protected ConfigChangedListener changedListener;
    protected ConfigCache configCache;

    public ConfigSyncManager(LocalPropsService localPropsService) {
        this.localPropsService = localPropsService;

        initial();
    }

    public ConfigSyncManager setChangedListener(ConfigChangedListener changedListener) {
        this.changedListener = changedListener;
        return this;
    }

    private void startIfClient() throws InterruptedException {
        // 根据IP及端口连接ZooKeeper
        if (client == null) {
            client = CuratorFrameworkFactory.builder()
            		.connectString(zookeeperUrl)
            		.sessionTimeoutMs(connectionTimeoutMs)
                    .connectionTimeoutMs(connectionTimeoutMs)
                    .retryPolicy(new RetryNTimes(3, 2000))
                    .build();

            client.start();
            client.getZookeeperClient().blockUntilConnectedOrTimedOut();
            if (!client.getZookeeperClient().isConnected()) {
                throw new IllegalStateException(
                        "can not connect zookeeper server in " + connectionTimeoutMs + "ms, url=" + zookeeperUrl);
            }
        }
    }

    /**
     * 启动客户端数据交换，并阻塞至初始化完成（或超时）
     */
    protected void initial() {

        this.localPropsService.ensureInitial();

        // 客户端启动时判断客户端属性
        this.checkClientConfig();
        // 获取本机IP
        this.detectLocalIp();

        try {
            LOG.info("启动加载配置...");

            this.startIfClient();

            // 版本校验(同级别,接近当前版本的最新版本)
            checkVersion();

            String tempPath = "";
            // 根据层级等封装路径,若路径不存在则自动创建
            Iterator<?> iterator = levels.keySet().iterator();
            while (iterator.hasNext()) {
                String l = levels.get(iterator.next());
                if (l == null || l.isEmpty())
                    continue;
                tempPath = tempPath + "/" + l;
                pathLevelList.add(propsPathPrefix + tempPath);
                if(LOG.isDebugEnabled()) {
					LOG.debug("path[{}]={}", pathLevelList.size() - 1, propsPathPrefix + tempPath);
                }
            }
            
			// // 配置路径
			// String configFilepath = tempPath;
			// LOG.debug("configFilepath = {}", configFilepath);
			// // 确认配置路径,若无则创建目录等
			// EnsurePath ensurePath = new EnsurePath(propsPathPrefix +
			// configFilepath);
			// ensurePath.ensure(client.getZookeeperClient());
			//
			// for (String configFilename : configFileNameList) {
			// usingPath = usingPathPrefix + tempPath + "/" + configFilename;
			// ensurePath = new EnsurePath(usingPath);
			// ensurePath.ensure(client.getZookeeperClient());
			// }

            // 客户端启动时加载,读取且保存当前配置属性值
            refreshConfig();
            LOG.info("完成加载配置");
        } catch (Exception exception) {
            LOG.error("加载配置异常", exception);
            throw new IllegalStateException("加载配置异常");
        } finally {
            this.stop();
        }
    }

    /**
     * 客户端启动时初始化,读取且保存当前配置属性值
     * 
     * @throws Exception
     */
    public synchronized void refreshConfig() throws Exception {
        try {
            
            this.startIfClient();
            LOG.info("开始刷新配置...");
            // 初始化 configCache
            configCache = new ConfigCache();

             //将本地多出来的配置存入
            for (String key : localPropsService.getLocalProps().keySet()) {
                if (!configFileNameList.contains(key)) {
                    LOG.info("$$使用本地配置-{}$$, VALUE = {}", key, 
                    		localPropsService.getLocalProps(key).toString());
                    configCache.configPropsMap.put(key, localPropsService.getLocalProps(key));
                    configCache.configPropsAll.putAll(localPropsService.getLocalProps(key));
                }
            }

            for (String configFilename : configFileNameList) {
            	// 将各级path下的configFilename配置按level层级合并
            	// 例如 Path有两个level： path[0]=/config/list/openapi，path[1]=/config/list/openapi/push
            	// 那么path[1]下的abc.properties配置就会覆盖path[0]下的abc.properties配置
                Properties remoteProps = new Properties();
                for(String configPath: pathLevelList) {
                    String tempPath = configPath + "/" + configFilename;
                    // 判断当前配置存在与否,若不存在则继续循环
                    if (null == client.checkExists().forPath(tempPath)) {
                    	LOG.debug("zk path {} is not exist.", tempPath);
                        continue;
                    }
                    byte[] data = client.getData().forPath(tempPath);
                    if (null == data || data.length == 0) {
                    	LOG.debug("zk path {} data is empty.", tempPath);
                        continue;
                    }
                    
                    Properties tempProps = new Properties();
                    tempProps.load(new ByteArrayInputStream(data));
                    // 判断properties,若为空则继续循环
                    if (tempProps.size() == 0) {
                    	LOG.debug("zk path {} properties is empty.", tempPath);
                        continue;
                    }
                    LOG.debug("got zk path {} data: {}", tempPath, tempProps.toString());
                    remoteProps.putAll(tempProps);
                    // 初始化时将配置保存至propsMap集合
                    // propsMap.put(configPath, tempProps);
                }

                // 根据设置的优先级别进行配置属性的添加或覆盖等
                setConfigProps(configFilename, remoteProps);

                // remotePropsList.add(remoteProps);
                // 比较配置及回调通知等
                if (configChangedHandlerMap.get(configFilename) != null) {
                    configChangedHandlerMap.get(configFilename).configChanged(remoteProps);
                } else {
                    ConfigChangedHandler configChangedHandler = new ConfigChangedHandler(
                    		configFilename, changedListener);
                    configChangedHandler.configChanged(remoteProps);
                    configChangedHandlerMap.put(configFilename, configChangedHandler);
                }
            }
            
            LOG.info("刷新配置完成");

            // 保存正在使用的配置属性等
            // saveConfigPropsToZookeeper();

        } finally {
            this.stop();
        }
    }

    /**
     * 保存配置
     * 
     * @throws Exception
     */
    protected void saveConfigPropsToZookeeper() {
        Properties props = new Properties();

        for (Map.Entry<String, Properties> entry : configCache.configPropsMap.entrySet()) {
            props.putAll(entry.getValue());
        }

        ByteArrayOutputStream fos = null;
        try {
            fos = new ByteArrayOutputStream();
            props.store(fos, "IP: " + localip);
            String tempPath = usingPath + "/" + randomPath;
            if (client.checkExists().forPath(tempPath) == null) {
                client.create().withMode(CreateMode.EPHEMERAL)
                .forPath(tempPath, fos.toByteArray());
            } else {
                client.setData().forPath(tempPath, fos.toByteArray());
            }
        } catch (Exception e) {
            LOG.error("saveConfigPropsToZookeeper error due to ", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 根据设置的优先级别进行配置属性的添加或覆盖等
     */
    protected void setConfigProps(String configFilename, Properties remoteProps) {
        Properties tmp = new Properties();
        switch (priority) {
        case 1: // 读取远程配置
            LOG.info("配置优先级别:读取远程配置-{}", configFilename);
            tmp = remoteProps;
            break;
        case 2: // 读取本地配置
            LOG.info("配置优先级别:读取本地配置-{}", configFilename);
            tmp = new Properties();
            Properties t = localPropsService.getLocalProps(configFilename);
            if (t != null) {
                tmp.putAll(t);
            }
            break;
        case 4: // 本地覆盖远程
            LOG.info("配置优先级别:本地覆盖远程-{}", configFilename);
            tmp = new Properties();
            tmp.putAll(remoteProps);
            Properties tp = localPropsService.getLocalProps(configFilename);
            if (tp != null) {
                tmp.putAll(tp);
            }
            break;
        default:
            // 远程覆盖本地
            LOG.info("配置优先级别:远程覆盖本地-{}", configFilename);
            tmp = new Properties();
            Properties tpp = localPropsService.getLocalProps(configFilename);
            if (tpp != null) {
                tmp.putAll(tpp);
            }
            tmp.putAll(remoteProps);
            break;
        }

        LOG.info("{} remote config value = {}", configFilename, 
        		Utils.CommonUtil.sortMapByKey(remoteProps, true).toString());
        LOG.info("{} final  config value = {}", configFilename, 
        		Utils.CommonUtil.sortMapByKey(tmp, true).toString());

        configCache.configPropsMap.put(configFilename, tmp);

        configCache.configPropsAll.putAll(tmp);
    }

    /**
     * 停止客户端数据交换
     */
    public synchronized void stop() {
        if (client != null) {
            client.close();
            client = null;
        }
    }

    public ConfigHolder getConfig() {
        return configCache;
    }
}