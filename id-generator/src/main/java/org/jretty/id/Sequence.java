package org.jretty.id;

import java.lang.management.ManagementFactory;

import org.jretty.util.IpUtils;

/**
 * 分布式高效有序ID生成器。（基于tweeter的snowflake算法）
 * 使用说明：参见README.md
 * 
 * @see SnowflakeId
 * 
 * @author zollty
 */
public class Sequence extends SnowflakeId {

    public Sequence() {
        setDatacenterId(getDatacenterId(maxDatacenterId));
        setWorkerId(getMaxWorkerId(getDatacenterId(), maxWorkerId));
    }

    /**
     * <p>
     * 数据标识id部分，根据mac地址获取（同一台机器，DatacenterId相同）
     * </p>
     */
    protected static long getDatacenterId(long maxDatacenterId) {
        long id = 0L;
        byte[] mac = IpUtils.getLocalMac();
        if (mac == null) {
            id = 1L;
        } else {
            id = ((0x000000FF & (long) mac[mac.length - 1])
                    | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
            id = id % (maxDatacenterId + 1);
        }
        return id;
    }

    /**
     * <p>
     * 获取 maxWorkerId，根据MAC + PID算出（同一个进程，WorkerId相同）
     * </p>
     */
    protected static long getMaxWorkerId(long datacenterId, long maxWorkerId) {
        StringBuilder mpid = new StringBuilder();
        mpid.append(datacenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (name != null && name.trim().length() > 0) {
            /*
             * GET jvmPid
             */
            mpid.append(name.split("@")[0]);
        }
        /*
         * MAC + PID 的 hashcode 获取16个低位
         */
        return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }
}
