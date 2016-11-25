/* 
 * Copyright (C) 2016-2017 the CSTOnline Technology Co.
 * Create by ZollTy on 2016-5-17 (zoutianyong@cstonline.com)
 */
package org.jretty.centralconfig;

/**
 * 
 * @author zollty
 * @since 2016-5-17
 */
public class ConfigConsts {
    
    /**
     * 配置中心文件路径
     */
    public static final String CENTRAL_CONFIG_FILE_PATH = "classpath:config/central-config.properties";
    public static final String CENTRAL_CONFIG_ID = "central-config.properties";
    /**
     * 中转kafka的consumer配置文件路径
     */
    public static final String TRANSIT_KAFKA_CONUSMER_CONFIG_PATH 
                    = "classpath:config/transit-kafka-conusmer.properties";
    public static final String TRANSIT_KAFKA_CONUSMER_ID = "push/transit-kafka-conusmer.properties";
    
    /**
     * kafkaapi项目的一般配置文件路径
     */
    public static final String KAFKAAPI_CONFIG_PATH                 
                    = "classpath:config/kafkaapi.properties";
    public static final String KAFKAAPI_FILE_ID = "push/kafkaapi.properties";
    
    
    /**
     * GDCP Kafka 的consumer配置文件路径
     */
    public static final String GDCP_KAFKA_CONUSMER_CONFIG_PATH 
                    = "classpath:config/gdcp-kafka-conusmer.properties";
    public static final String GDCP_KAFKA_CONUSMER_ID 
                    = "push/gdcp-kafka-conusmer.properties";
    
    /**
     * 中转kafka的consumer配置文件路径
     */
    public static final String TRANSIT_KAFKA_PRODUCER_CONFIG_PATH 
                    = "classpath:config/transit-kafka-producer.properties";
    public static final String TRANSIT_KAFKA_PRODUCER_ID 
                    = "push/transit-kafka-producer.properties";

}
