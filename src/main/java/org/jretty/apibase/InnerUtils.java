/* 
 * Copyright (C) 2018-2025 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2018-4-6 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.apibase;

/**
 * package inner utils
 * 
 * @author zollty
 * @since 2018/4/6
 */
class InnerUtils {
    private static final String REPLACE_LABEL = "{}";
    
    /**
     * 用objs[]的值去替换字符串s中的{}符号
     */
    public static String replaceParams(Object msg, Object... objs) {
        if (msg == null) {
            return null;
        }
        String s = msg.toString();
        if (objs == null || objs.length == 0) {
            return s;
        }
        if (!s.contains(REPLACE_LABEL)) {
            StringBuilder result = new StringBuilder();
            result.append(s);
            for (Object obj : objs) {
                result.append(" ").append(obj);
            }
            return result.toString();
        }

        String[] strarr = new String[objs.length];
        int len = s.length();
        for (int i = 0; i < objs.length; i++) {
            if (objs[i] != null) {
                strarr[i] = objs[i].toString();
            } else {
                strarr[i] = "null";
            }
            len += strarr[i].length();
        }

        StringBuilder result = new StringBuilder(len);
        int cursor = 0;
        int index = 0;
        for (int start; (start = s.indexOf(REPLACE_LABEL, cursor)) != -1;) {
            result.append(s, cursor, start);
            if (index < strarr.length) {
                result.append(strarr[index]);
            } else {
                result.append(REPLACE_LABEL);
            }
            cursor = start + 2;
            index++;
        }
        result.append(s, cursor, s.length());
        if (index < objs.length) {
            for (int i = index; i < objs.length; i++) {
                result.append(" ").append(objs[i]);
            }
        }
        return result.toString();
    }

}
