/* 
 * Copyright (C) 2015-2016 the original author or authors.
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
package org.jretty.apibase.dto;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

/**
 * 泛型结果类（包含返回data）<br/>
 * 
 * Result&lt;T&gt; result = Result.create();<br/>
 * ...<br/>
 * return result.success();<br/>
 * or <br/>
 * return result.success(data);<br/>
 * or <br/>
 * return result.fail("SomeErrorCode","SomeDescription")<br/>
 * or <br/>
 * return result.fail("SomeErrorCode") <br/>
 * <br/>
 * 
 * or you can do chained callings like below:<br/><br/>
 *
 * result.data(data).code("SomeCode").msg("SomeDescription").success(); <br/>
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
    
    private IMsg iMsg;
    
    private boolean putData = false;
    
    public Result() {
        setTimestamp(System.currentTimeMillis());
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        if (data != null) {
            this.data = data;
            super.put("data", data);
        }
    }
    
    public void setLocale(String locale) {
        if (iMsg != null && locale != null) {
            this.setMsg(iMsg.getMsg(locale));
        }
    }

    /**
     * 失败或者返回数据为空
     * 
     * @return
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
        if (putData) {
            return false;
        }
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
    
    public static <T> Result<T> success(Map<String, ?> map) {
        Result<T> result = new Result<T>();
        result.setSuccess(true);
        if (map != null && !map.isEmpty()) {
            result.putData = true;
            result.putAll(map);
        }
        return result;
    }

    public static <T> Result<T> fail(String code, String description) {
        Result<T> result = new Result<T>();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(description);
        return result;
    }

    public static <T> Result<T> fail(BaseResult baseResult) {
        return fail(baseResult.getCode(), baseResult.getMsg());
    }
    
    public static <T> Result<T> fail(IMsg msg) {
        final Result<T> result = new Result<T>();
        result.setSuccess(false);
        result.iMsg = msg;
        result.setCode(msg.getCode());
        result.setMsg(msg.getMsg());
        return result;
    }

    public static <T> Result<T> fail(String msg) {
        return fail("500", msg);
    }
    
    public Result<T> code(String code) {
        this.setCode(code);
        return this;
    }

    public Result<T> msg(String msg) {
        this.setMsg(msg);
        return this;
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
    
    public Result<T> put(String key, Object value) {
        this.putData = true;
        super.put(key, value);
        return this;
    }
}