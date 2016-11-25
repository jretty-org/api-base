/* 
 * Copyright (C) 2016-2017 the CSTOnline Technology Co.
 * Create by ZollTy on 2016-5-16 (zoutianyong@cstonline.com)
 */
package org.jretty.centralconfig;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 集中配置管理器的父类
 * 
 * @author zollty
 * @since 2016-5-16
 */
public abstract class AbstractConfigSyncManager {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractConfigSyncManager.class);

    // 路径,即根据value值与配置文件名称生成配置文件路径
    protected TreeMap<Integer, String> levels = new TreeMap<Integer, String>();
    protected Map<Integer, Boolean> isVersions = new HashMap<Integer, Boolean>();
    // 远程与本地优先级别,包括单一读取和覆盖优先级别
    // 1.只取远程2.只取本地3.远程覆盖本地4.本地覆盖远程
    protected Integer priority;
    // 远程读properties取或保存路径前缀值
    protected String propsPathPrefix = "";
    protected String usingPathPrefix = "";
    protected String usingPath = "";
    // 远程配置连接URL
    protected String zookeeperUrl = "";
    // 远程配置文件名称
    protected List<String> configFileNameList = new ArrayList<String>();

    protected CuratorFramework client = null;

    protected String localip;

    protected LocalPropsService localPropsService;

    /**
     * 判断zookeeper配置属性值 优先级别setter>local>default值
     */
    protected void checkClientConfig() {
        // 检测基本配置
        propsPathPrefix = localPropsService.getNotEmptyValue("configcenter.zookeeper.props.prefix");
        usingPathPrefix = localPropsService.getNotEmptyValue("configcenter.zookeeper.using.prefix");
        zookeeperUrl = localPropsService.getNotEmptyValue("configcenter.zookeeper.url");
        String tmp = localPropsService.getNotEmptyValue("configcenter.config.filename");
        String priorityStr = localPropsService.getNotEmptyValue("configcenter.config.priority");

        if (propsPathPrefix == null || usingPathPrefix == null || zookeeperUrl == null || tmp == null
                || priorityStr == null) {
            throw new IllegalStateException(
                    "configcenter basic config(zookeeper,filename,priority...) can not be empty.");
        }
        
        configFileNameList = Arrays.asList(tmp.split(","));
        try {
            priority = Integer.parseInt(priorityStr);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("{configcenter.config.priority} can not be parse to Integer.");
        }

        this.initialAppConfigPathLevel();
    }
    
    /**
     * 设置应用的层级路径<br>
     * 
     * 设置方法1： configcenter.levels = openapi/push<br>
     * 设置方法2： <br>
     *   configcenter.level.1 = openapi<br>
     *   configcenter.level.2 = push<br>
     * 设置方法3： <br>
     *   configcenter.level.system = openapi<br>
     *   configcenter.level.server = push<br>
     * 
     * 这三种设置方式是等价的。<br>
     * <br>
     * 定义好的level层级如下：<br>
     * 1.system 2.server 3.version 4.clusterid 5.slaveid<br>
     */
    protected void initialAppConfigPathLevel() {
    	String[] paths = localPropsService.getStringDefValue("configcenter.levels", "", "").split("/");
        Properties isversions = parseIsVersions(
                localPropsService.getStringDefValue("configcenter.levels.isversions", "", ""));

        for (int n = 1; n <= 9; n++) {
            if (trimVal(getLevel(n)).isEmpty()) {
            	
				setLevel(n, localPropsService.getStringDefValue("configcenter.level." + n, "",
						n <= paths.length ? paths[n - 1] : null));
				
				setIsVersion(n,Boolean.parseBoolean(
								localPropsService.getStringDefValue(
										"configcenter.level." + n + ".isversion", "",
										isversions.getProperty(String.valueOf(n), "false"))));
			}
        }

        // 候选属性名
        if (trimVal(getLevel(1)).isEmpty())
            setSystem(localPropsService.getStringDefValue("configcenter.level.system", getSystem(), ""));
        if (trimVal(getLevel(2)).isEmpty())
            setServer(localPropsService.getStringDefValue("configcenter.level.server", getServer(), ""));
        if (trimVal(getLevel(3)).isEmpty())
            setVersion(localPropsService.getStringDefValue("configcenter.level.version", getVersion(), ""));
        if (trimVal(getLevel(4)).isEmpty())
            setClusterID(localPropsService.getStringDefValue("configcenter.level.clusterid", getClusterID(), ""));
        if (trimVal(getLevel(5)).isEmpty())
            setSlaveID(localPropsService.getStringDefValue("configcenter.level.slaveid", getSlaveID(), ""));
    }

    protected void detectLocalIp() {
        List<String> zk = new ArrayList<String>(Arrays.asList(zookeeperUrl.split(",")[0].split("\\:")));
        if (zk.size() < 2)
            zk.add("2181");

        try {
            Socket socket = new Socket(zk.get(0), Integer.parseInt(zk.get(1)));
            this.localip = socket.getLocalAddress().getHostAddress();
            socket.close();
            LOG.debug("通过 socket.connect 的方式，确定本地 ip 为 {}", localip);
        } catch (Exception e) {
            LOG.warn("通过 {} 确定本地 IP 时错误", zookeeperUrl, e);
            try {
                this.localip = InetAddress.getLocalHost().getHostAddress();
            } catch (IOException e1) {

            }
        }
    }

    protected void checkVersion() throws Exception {
        // 若版本参数集合为空或大小等于零时,停止执行后续判断
        if (null == isVersions || isVersions.size() == 0) {
            return;
        }
        // 集合主键迭代器
        Iterator<Integer> iterator = levels.keySet().iterator();
        String selfPath = propsPathPrefix;
        String parentPath = "";
        Integer level = new Integer(1);
        while (iterator.hasNext()) {
            level = iterator.next();
            if (levels.get(level) == null || levels.get(level).isEmpty())
                continue;
            parentPath = selfPath;
            selfPath = selfPath + "/" + levels.get(level);
            // 判断该层级是否属于版本,若是则判断该版本是否已创建
            if (getIsVersion(level)) {
                // 判断是否符合版本格式
                if (!validateVersion(levels.get(level))) {
                    continue;
                }
                Stat stat = client.checkExists().forPath(selfPath);
                if (null == stat) {
                    // 判断上级目录是否已创建,若未建则中断
                    stat = client.checkExists().forPath(parentPath);
                    if (null == stat) {
                        break;
                    }
                    // 根据父目录路径获取同级别版本等,若无同级别则中断
                    List<String> pathArray = client.getChildren().forPath(parentPath);
                    if (null == pathArray || pathArray.size() == 0) {
                        break;
                    }
                    String version = levels.get(level);
                    // 根据大小版本号递归比较,以便取得同级别版本等
                    String maxVersion = null;
                    String minVersion = null;
                    for (String path : pathArray) {
                        // 判断是否符合版本格式
                        if (!validateVersion(path)) {
                            continue;
                        }
                        if (null == minVersion || compareVersion(path, minVersion) < 0) {
                            minVersion = path;
                        }
                        if (compareVersion(path, version) > 0) {
                            continue;
                        } else if (compareVersion(path, maxVersion) > 0) {
                            maxVersion = path;
                        }
                    }
                    if (null != maxVersion) {
                        levels.put(level, maxVersion);
                    } else if (null != minVersion) {
                        levels.put(level, minVersion);
                    }
                    LOG.info("加载配置路径:{}", selfPath);
                } else {
                    continue;
                }
            }
        }
    }

    private static boolean validateVersion(String version) {
        return regexVersion.matcher(version).matches();
    }

    private static final Pattern regexVersion = Pattern.compile("\\d+(\\.\\d+)*");

    private int compareVersion(String path, String version) {
        int returnVal = -1;
        if (null == version) {
            returnVal = 1;
        } else {
            String[] pathArray = path.split("\\.");
            String[] versionArray = version.split("\\.");
            int length = pathArray.length < versionArray.length ? pathArray.length : versionArray.length;
            // 根据长度最短的数组比较值,若根据最短长度未比较出结果
            // 则数组长度大的值大于长度小的值
            for (int i = 0; i < length; i++) {
                if (Integer.parseInt(pathArray[i]) > Integer.parseInt(versionArray[i])) {
                    returnVal = 1;
                    break;
                }
                if (Integer.parseInt(pathArray[i]) == Integer.parseInt(versionArray[i])) {
                    continue;
                } else {
                    break;
                }
            }
            if (returnVal == -1 && pathArray.length > versionArray.length) {
                returnVal = 1;
            }
        }
        return returnVal;
    }

    protected String trimVal(String val) {
        return val == null ? "" : val.trim();
    }

    public void setLocalPropsService(LocalPropsService localPropsService) {
        this.localPropsService = localPropsService;
    }

    public String getPropsPathPrefix() {
        return propsPathPrefix;
    }

    public void setPropsPathPrefix(String propsPathPrefix) {
        this.propsPathPrefix = propsPathPrefix;
    }

    public String getUsingPathPrefix() {
        return usingPathPrefix;
    }

    public void setUsingPathPrefix(String usingPathPrefix) {
        this.usingPathPrefix = usingPathPrefix;
    }

    public String getZookeeperUrl() {
        return zookeeperUrl;
    }

    public void setZookeeperUrl(String zookeeperUrl) {
        this.zookeeperUrl = zookeeperUrl;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Map<Integer, String> getLevelsMap() {
        return levels;
    }

    public Map<Integer, Boolean> getIsVersionsMap() {
        return isVersions;
    }

    public void setLevelsMap(Map<Integer, String> levels) {
        this.levels.clear();
        this.levels.putAll(levels);
    }

    public void setIsVersionsMap(Map<Integer, Boolean> isVersions) {
        this.isVersions.clear();
        this.isVersions.putAll(isVersions);
    }

    public void setLevel(int level, String name) {
        levels.put(level, name);
    }

    public String getLevel(int level) {
        return levels.get(level);
    }

    public void setIsVersion(int level, boolean is) {
        if (is)
            isVersions.put(level, is);
        else
            isVersions.remove(level);
    }

    public boolean getIsVersion(int level) {
        return isVersions.get(level) != null ? isVersions.get(level) : false;
    }

    public void setLevels(String path) {
		if (path == null || path.trim().length() == 0) {
			return;
		}
		String[] paths = path.split("/");
		this.levels.clear();
		for (int i = 0; i < paths.length; i++) {
			this.levels.put(i + 1, paths[i]);
		}
    }

    public void setIsVersions(String isversions) {
        isVersions.clear();
        Properties props = parseIsVersions(isversions);
        for (Map.Entry<Object, Object> e : props.entrySet()) {
            try {
                isVersions.put(Integer.parseInt(e.getKey().toString()), true);
            } catch (NumberFormatException ex) {
            }
        }
    }

    private Properties parseIsVersions(String isversions) {
        Properties props = new Properties();
        for (String v : isversions.split(",")) {
            v = v.trim();
            if (!v.isEmpty()) {
                props.put(v, "true");
            }
        }
        return props;
    }

    public void setSystem(String system) {
        setLevel(1, system);
        setIsVersion(1, false);
    }

    public String getSystem() {
        return getLevel(1);
    }

    public void setServer(String server) {
        setLevel(2, server);
        setIsVersion(2, false);
    }

    public String getServer() {
        return getLevel(2);
    }

    public void setVersion(String version) {
        setLevel(3, version);
        setIsVersion(3, validateVersion(version));
    }

    public String getVersion() {
        return getLevel(3);
    }

    public void setClusterID(String cluster) {
        setLevel(4, cluster);
        setIsVersion(4, false);
    }

    public String getClusterID() {
        return getLevel(4);
    }

    public void setSlaveID(String slave) {
        setLevel(5, slave);
        setIsVersion(5, false);
    }

    public String getSlaveID() {
        return getLevel(5);
    }

    public List<String> getConfigFileNameList() {
        return configFileNameList;
    }

    public void setConfigFileNameList(List<String> configFileNameList) {
        this.configFileNameList = configFileNameList;
    }

}