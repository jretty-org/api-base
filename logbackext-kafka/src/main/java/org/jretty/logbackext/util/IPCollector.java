package org.jretty.logbackext.util;

public class IPCollector {

    public static String getLocalIp() {
        return IpUtils.getLocalIP();
    }

    public static String getHostId() {
        String ip = IpUtils.getLocalIP();
        if (ip != null) {
            return ip;
        }
        return IpUtils.getHostName();
    }

}