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

import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

/**
 * 泛型结果类（包含返回data）<br/>
 * 
 * return Result.success();<br/>
 * or <br/>
 * return result.success(data);<br/>
 * or <br/>
 * return result.fail("some description")<br/>
 * or <br/>
 * return result.fail(IMsg msg) <br/>
 * <br/>
 * 
 * or you can do chained callings like below:<br/><br/>
 *
 * result.success().put("user", "admin").put("menuList", menuList); <br/>
 * 
 * @author zollty
 * @since 2015/9/14
 */
public class Result<T> extends BaseResult {
    private static final long serialVersionUID = 1L;
    /**
     * 返回数据，可为基本类型（包装类），可以为其它可序列化对象
     */
    private T data;
    
    private IMsg imsg;
    private Object[] params;
    
    public Result() {
        setTimestamp(System.currentTimeMillis());
    }

    @SuppressWarnings("unchecked")
    public T getData() {
        return (T) super.get("data");
    }

    private void setData(T data) {
        if (data != null) {
            this.data = data;
            super.put("data", data);
        }
    }
    
    private void setImsg(IMsg imsg, Object... params) {
        this.imsg = imsg;
        this.params = params;
    }
    
    public void setLocale(String locale) {
        if (imsg != null && locale != null) {
            this.setMsg(InnerUtils.replaceParams(imsg.getMsg(locale), params));
        }
    }
    
    /**
     * !isSuccess()
     */
    public boolean isFailed() {
        return !isSuccess();
    }

    /**
     * 失败或者返回数据为空
     */
    public boolean isFailedOrEmpty() {
        return !isSuccess() || dataEmpty();
    }

    /**
     * 数据是否为空<br/>
     * 如果是对象,则判断是否为空（NULL）<br/>
     * 如果是字符串,则判断是为空串（NULL,""）<br/>
     * 如果是Collection(List等)对象,则判断是否为空列表（isEmpty)<br/>
     * 如果是Map对象,则判断是否为空Map(isEmpty)<br/>
     * 如果是Array数组，则判断是否为空数组(length == 0)
     * 
     * @return TRUE if data is null or empty
     */
    public boolean dataEmpty() {
        // 默认就有三个字段：{"success":true,"code":"200","timestamp":1500117047167}
        if (super.size() > 3) {
            return false;
        }
        data = getData();
        if (data == null) {
            return true;
        }
        if (data instanceof String) {
            String str = (String) data;
            return "".equals(str);
        } else if (data instanceof Collection) {
            Collection<?> list = (Collection<?>) data;
            return list.isEmpty();
        } else if (data instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) data;
            return map.isEmpty();
        } else if (data instanceof Object[]) {
            Object[] array = (Object[]) data;
            return array.length == 0;
        } else if (data instanceof Enumeration) {
            Enumeration<?> enu = (Enumeration<?>) data;
            return enu.hasMoreElements();
        }
        return false;
    }

    // ～～～～～～～～～～～～～以下是链式编程方法，用于构造Result对象
    public static <T> Result<T> success() {
        return success((T) null);
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<T>();
        result.setSuccess(true);
        result.setData(data);
        return result;
    }
    
    public static <T> Result<T> success(Map<?, ?> map) {
        Result<T> result = new Result<T>();
        result.setSuccess(true);
        if (map != null && !map.isEmpty()) {
            result.putAll(map);
        }
        return result;
    }

    public static <T> Result<T> fail() {
        Result<T> result = new Result<T>();
        result.setSuccess(false);
        result.setCode(BaseResult.defaultErrorCode);
        return result;
    }
    
    public static <T> Result<T> fail(String description) {
        Result<T> result = new Result<T>();
        result.setSuccess(false);
        result.setCode(BaseResult.defaultErrorCode);
        result.setMsg(description);
        return result;
    }
    
    public static <T> Result<T> fail(String code, String description) {
        Result<T> result = new Result<T>();
        result.setSuccess(false);
        result.setCode(code != null ? code : BaseResult.defaultErrorCode);
        result.setMsg(description);
        return result;
    }
    
    public static <T> Result<T> fail(IMsg imsg, Object... params) {
        final Result<T> result = new Result<T>();
        result.setSuccess(false);
        result.imsg(imsg, params);
        return result;
    }
    
    /** 创建一个Result对象，稍后才为其设置success状态和data等值。success默认为false */
    public static <T> Result<T> create() {
        Result<T> result = new Result<T>();
        result.setSuccess(false);
        return result;
    }
    
    /** 创建一个Result对象并设置data，稍后才为其设置success状态和其他数据。success默认为true */
    public static <T> Result<T> create(T data) {
        return success(data);
    }
    
    public Result<T> code(String code) {
        this.setCode(code);
        return this;
    }

    public Result<T> msg(String msg) {
        this.setMsg(msg);
        return this;
    }
    
    public Result<T> success(boolean success) {
        this.setSuccess(success);
        return this;
    }
    
    public Result<T> failed() {
        return success(false);
    }

    public Result<T> sid(String sid) {
        this.setSid(sid);
        return this;
    }
    
    public Result<T> timestamp(Long timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public Result<T> data(T data) {
        this.setData(data);
        return this;
    }
    
    public Result<T> imsg(IMsg imsg, Object... params) {
        this.setImsg(imsg, params);
        this.setCode(imsg.getCode());
        this.setMsg(InnerUtils.replaceParams(imsg.getMsg(), params));
        return this;
    }
    
    public Result<T> put(Object key, Object value) {
        super.put(key, value);
        return this;
    }
    
    public Result<T> putMap(Map<Object, Object> m) {
        super.putAll(m);
        return this;
    }

    public static void setDefaultCode(String defaultErrorCode, String defaultSuccessCode) {
        BaseResult.defaultErrorCode = defaultErrorCode;
        BaseResult.defaultSuccessCode = defaultSuccessCode;
    }
    
    // override super
    /**
     * @return the sid
     */
    public String getSid() {
        return (String) super.get("sid");
    }
    
    /**
     * @return the success
     */
    public Boolean isSuccess() {
        return (Boolean) super.get("success");
    }
    
    /**
     * @return the code
     */
    public String getCode() {
        return (String) super.get("code");
    }
    
    /**
     * @return the description
     */
    public String getMsg() {
        return (String) super.get("msg");
    }
    
    public Long getTimestamp() {
        return (Long) super.get("timestamp");
    }
}