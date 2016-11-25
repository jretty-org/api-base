package org.jretty.logbackext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPoolConfig;

/**
 * 用Redis定时推送数据
 * 
 * @author zollty
 * @since 2016-9-3
 */
public abstract class TimingRedisAppender extends TimingAppender {

    protected RedisTemplate redisTemplate;

    protected String redisKey;

    protected String redisField;

    private String clientId;

    /**
     * 定时获取要推送的消息
     * 
     * @return 要推送的消息
     */
    protected abstract String getPushMsg();

    @Override
    protected Runnable getRunnable() {

        return new Runnable() {
            Logger logger = LoggerFactory.getLogger(TimingRedisAppender.class);
            @Override
            public void run() {
                String msg = null;
                try {
                    msg = buildPushContent();
                } catch (Exception e) {
                    logger.error("get push msg error due to ", e);
                }
                if (msg != null) {
                    try {
                        if (redisField == null) {
                            redisTemplate.getJedisCluster().rpush(redisKey, msg);
                        } else {
                            redisTemplate.getJedisCluster().hset(redisKey, redisField, msg);
                        }
                    } catch (Exception e) {
                        logger.error("push msg to redis error due to ", e);
                    }
                }
            }
        };
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = parseParam(redisKey);
    }

    public void setRedisField(String redisField) {
        this.redisField = parseParam(redisField);
    }

    public void setRedisServers(String redisServers) {
        this.redisTemplate = new RedisTemplate();
        this.redisTemplate.setRedisServers(redisServers);
        this.redisTemplate.setJedisPoolConfig(getPoolCfg());
    }

    private JedisPoolConfig getPoolCfg() {
        JedisPoolConfig jpc = new JedisPoolConfig();
        jpc.setMaxIdle(8);
        return jpc;
    }

    public String getClientId() {
        if (clientId == null) {
            clientId = IpUtils.getHostName() + "$" + IpUtils.getLocalIP();
        }
        return clientId;
    }

    protected String buildPushContent() {
        String msg = getPushMsg();
        if (msg != null) {
            return String.format("{\"__source__\":\"%s\",\"__time__\":%s,\"__logs__\":%s}", 
                    getClientId(),
                    String.valueOf(System.currentTimeMillis() / 1000L), msg);
        }
        return null;
    }

    private String parseParam(String param) {
        if (param == null || param.trim().length() == 0) {
            return null;
        }
        String ret = param;
        if (param.contains("@clientId")) {
            ret = param.replace("@clientId", getClientId());
        }
        if (param.contains("@ip")) {
            ret = param.replace("@ip", IpUtils.getLocalIP());
        }
        return ret;
    }
}