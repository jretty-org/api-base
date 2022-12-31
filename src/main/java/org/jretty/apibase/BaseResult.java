/* 
 * Copyright (C) 2015-2017 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2015-9-14 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.apibase;

import java.util.HashMap;

/**
 * 基础回复类
 * 
 * @author zollty
 * @since 2015/9/14
 */
public class BaseResult extends HashMap<Object, Object> {
    private static final long serialVersionUID = 2991693728111260232L;
    protected static String defaultSuccessCode = "200";
    protected static String defaultErrorCode = "500";
    /**
     * 返回标识号
     */
    private String sid;

    /** 成功标志 */
    private Boolean success;

    /** 信息码 */
    private String code;

    /** 描述 */
    private String msg;
    
    private Long timestamp;

    /**
     * @return the sid
     */
    public String getSid() {
        return sid;
    }

    /**
     * @param sid 唯一ID
     */
    public void setSid(String sid) {
        this.sid = sid;
        super.put("sid", sid);
    }

    /**
     * @return the success
     */
    public Boolean isSuccess() {
        return success;
    }

    /**
     * @param success
     */
    public void setSuccess(boolean success) {
        this.success = success;
        super.put("success", success);
        this.setCode(defaultSuccessCode);
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
        super.put("code", code);
    }

    /**
     * @return the description
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
        super.put("msg", msg);
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        super.put("timestamp", timestamp);
    }

}