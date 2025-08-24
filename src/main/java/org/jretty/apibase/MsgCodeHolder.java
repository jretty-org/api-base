/*
 * Copyright (C) 2021-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2021-10 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.apibase;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zollty
 */
public class MsgCodeHolder {

    // 单例模式
    private static class Inner {
        private static final Map<String, Map<String, String>> PRIVATE_MAP = new HashMap<String, Map<String, String>>();
    }

    public static Map<String, Map<String, String>> getMsgsMap() {
        return Inner.PRIVATE_MAP;
    }

    /**
     * 根据code获取Message定义，从指定group中获取（例如AppException、MsgBase）
     *
     * @param code  Msg编码
     * @param group （例如AppException、MsgBase）
     */
    public static String getMsg(String code, String group) {
        if (group != null) {
            Map<String, String> msgs = Inner.PRIVATE_MAP.get(group);
            if (msgs != null) {
                return msgs.get(code);
            }
            return null;
        }
        return getMsg(code);
    }

    /**
     * 根据code获取Message定义，从所有group中遍历（例如AppException、MsgBase）
     *
     * @param code Msg编码
     */
    public static String getMsg(String code) {
        Map<String, Map<String, String>> msgsMap = getMsgsMap();
        for (Map.Entry<String, Map<String, String>> entry : msgsMap.entrySet()) {
            String message = entry.getValue().get(code);
            if (message != null) {
                // 找到后返回
                return message;
            }
        }
        // 未找到
        return null;
    }

    public static Map<String, String> addMsg(Class<? extends Enum<?>> enumClass) {
        Map<String, String> msgs = new LinkedHashMap<String, String>();
        for (Enum<?> e : enumClass.getEnumConstants()) {
            IMsg msg = (IMsg) e;
            msgs.put(msg.getCode(), msg.getMessage());
        }
        Inner.PRIVATE_MAP.put(enumClass.getSimpleName(), msgs);
        return msgs;
    }

}
