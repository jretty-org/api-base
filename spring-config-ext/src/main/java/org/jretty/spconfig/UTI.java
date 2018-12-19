package org.jretty.spconfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * 工具类入口
 * @author zollty
 * @since 2018年3月1日
 */
class UTI {
    
    private static final Log LOG = LogFactory.getLog(UTI.class);

    static class Props {

        /**
         * 获取Properties资源
         */
        public static Properties getProperties(Resource location) {
            InputStream io = null;
            try {
                io = location.getInputStream();
                Properties tempProps = new Properties();
                tempProps.load(io);
                return tempProps;
            } catch (IOException e) {
                LOG.warn("getProperties error due to ", e);
            } finally {
                if (io != null) {
                    try {
                        io.close();
                    } catch (IOException e) {
                        // ignore...
                    }
                }
            }
            return null;
        }

        public static Properties readConf(PathMatchingResourcePatternResolver resolver, String location) {
            if (location == null) {
                return null;
            }
            Resource res = resolver.getResource(location);
            if (res == null || !res.exists()) {
                LOG.error("can not find config : " + location);
                return null;
            }
            return getProperties(res);
        }

        /**
         * 使用 Map按key进行排序
         */
        public static Map<String, String> sortMapByKey(Properties props, final boolean asc) {
            if (props == null) {
                return null;
            }
            Map<String, String> map = covertProperties2Map(props);
            return sortMapByKey(map, asc);
        }

        /**
         * 使用 Map按key进行排序
         */
        @SuppressWarnings("unchecked")
        public static Map<String, String> sortMapByKey(Map<String, String> map, final boolean asc) {
            if (map == null) {
                return null;
            }
            if (map.isEmpty()) {
                return Collections.EMPTY_MAP;
            }
            Map<String, String> sortMap = new TreeMap<String, String>(new Comparator<String>() {
                @Override
                public int compare(String str1, String str2) {
                    return asc ? str1.compareTo(str2) : str2.compareTo(str1);
                }
            });
            sortMap.putAll(map);
            return sortMap;
        }

        /**
         * 将Properties资源转换成Map类型
         */
        @SuppressWarnings("unchecked")
        public static Map<String, String> covertProperties2Map(Properties props) {
            if (props == null) {
                throw new IllegalArgumentException("props=null");
            }

            if (props.isEmpty()) {
                return Collections.EMPTY_MAP;
            }

            Set<Entry<Object, Object>> set = props.entrySet();

            Map<String, String> mymap = new HashMap<String, String>(set.size());
            for (Entry<Object, Object> oo : set) {
                mymap.put(oo.getKey().toString(), oo.getValue().toString());
            }
            return mymap;
        }

    }
}
