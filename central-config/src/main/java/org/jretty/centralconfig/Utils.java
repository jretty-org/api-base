package org.jretty.centralconfig;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

class Utils {

    private Utils() {
    }

    public static class CommonUtil {
        /**
         * 使用 Map按key进行排序
         */
        @SuppressWarnings("unchecked")
        public static Map<String, String> sortMapByKey(Properties props, final boolean asc) {
            if (props == null) {
                return null;
            }

            Map<String, String> map = covertProperties2Map(props);

            if (props.isEmpty()) {
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