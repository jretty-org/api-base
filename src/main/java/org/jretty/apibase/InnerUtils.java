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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * package inner utils
 * 
 * @author zollty
 * @since 2018/4/6
 */
class InnerUtils {
    private static final String REPLACE_LABEL = "{}";
    /**
     * MessageDigest Algorithm: MD5
     */
    private static final String MD5 = "MD5";
    private static final String[] CHARS = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
            "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z"};
    
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

    public static String shortMsg(String msg) {
        // MD5 加密
        String hex = crypt(msg, MD5);

        // 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
        String tempStr = hex.substring(0, 8);
        // 这里需要使用 long 型来转换，因为 Inteper .parseInt() 只能处理 31 位 , 首位为符号位 , 如果不用
        // long ，则会越界
        // 与0x3FFFFFFF位与运算的目的是获得
        long lHexLong = 0x0FFFFFFF & Long.parseLong(tempStr, 16);
        StringBuilder sbu = new StringBuilder(7);
        for (int j = 0; j < 6; j++) {
            // 把得到的值与 0x0000003D (61)进行位与运算，取得字符数组 chars 索引
            long index = 0x0000003D & lHexLong;
            // 把取得的字符相加
            sbu.append(CHARS[(int) index]);
            // 每次循环按位右移 5 位
            lHexLong = lHexLong >> 5;
        }
        // 把字符串存入对应索引的输出数组
        return sbu.toString();
    }

    public static String toHexStr(byte[] data) {
        if (data == null) {
            return null;
        } else if (data.length == 0) {
            return "";
        } else {
            StringBuilder hexString = new StringBuilder(33);

            for(int i = 0; i < data.length; ++i) {
                String stmp = Integer.toHexString(255 & data[i]);
                if (stmp.length() == 1) {
                    hexString.append('0').append(stmp);
                } else {
                    hexString.append(stmp);
                }
            }

            return hexString.toString();
        }
    }

    public static String crypt(String str, String algorithm) {
        if (str==null || str.isEmpty()) {
            throw new IllegalArgumentException("String to encrypt cannot be null or zero length");
        } else {
            try {
                MessageDigest msgdig = MessageDigest.getInstance(algorithm);
                msgdig.update(str.getBytes("UTF-8"));
                byte[] hash = msgdig.digest();
                return toHexStr(hash);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e.toString());
            }
        }
    }
}
