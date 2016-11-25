package org.jretty.logbackext.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author zollty
 * @since 2016-7-28
 */
class IpUtils {
    
    private static final Logger LOG = LoggerFactory.getLogger(IpUtils.class);


    private static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }
    
    public static String getLocalIP() {
        return getLocalIP("eth0");
    }

    /**
     * 获取本机Local ip（内网）地址，并自动区分Windows还是linux操作系统
     * 注意：Local IP标准的定义：（参见java.net.InetAddress.isSiteLocalAddress()）
     * // refer to RFC 1918
     * // 10/8 prefix
     * // 172.16/12 prefix
     * // 192.168/16 prefix
     * 但是，这个方法扩大了这个定义的范围，只要是xxx.xxx.xxx.xxx（xxx为数字）格式的，都是本机IP
     * 
     * @return String IP地址，比如 172.16.14.160,或者null
     */
    public static String getLocalIP(String networkInterfaceName) {
        InetAddress ip = null;
        try {
            // 如果是Windows操作系统
            if (isWindowsOS()) {
                ip = InetAddress.getLocalHost();
            }
            // 如果是Linux操作系统
            else {
                Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) 
                        NetworkInterface.getNetworkInterfaces();
                boolean bFindIP = false;
                while (netInterfaces.hasMoreElements()) {
                    if (bFindIP) {
                        break;
                    }
                    NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                    // ----------特定情况，可以考虑用ni.getName判断
                    if (!ni.getName().equals(networkInterfaceName)) {
                        continue;
                    }
                    // 遍历所有ip
                    Enumeration<InetAddress> ips = ni.getInetAddresses();
                    InetAddress tmp;
                    while (ips.hasMoreElements()) {
                        tmp = (InetAddress) ips.nextElement();
                        if (!tmp.isLoopbackAddress() // 127.开头的都是lookback地址
                                && tmp.getHostAddress().indexOf(":") == -1) {
                            ip = tmp;
                            bFindIP = true;
                            break;
                        }
                    }

                }
            }
        } catch (Exception e) {
            LOG.warn("get ip fail due to: ", e);
        }

        String sIP = null;
        if (null != ip) {
            sIP = ip.getHostAddress();
        }
        return sIP;
    }
    
    /**
     * 获取HostName
     * @return String HostName or null
     */
    public static String getHostName() {
        String hostName = null;
        if (System.getenv("COMPUTERNAME") != null) {
            hostName = System.getenv("COMPUTERNAME");
        } else {
            try {
                hostName = (InetAddress.getLocalHost()).getHostName();
            } catch (UnknownHostException uhe) {
                String host = uhe.getMessage();
                if (host != null) {
                    int colon = host.indexOf(':');
                    if (colon > 0) {
                        hostName = host.substring(0, colon);
                    }
                }
            }
        }
        // 转换成大写，以避免大小写问题
        return hostName != null ? hostName.toUpperCase() : null;
    }


}