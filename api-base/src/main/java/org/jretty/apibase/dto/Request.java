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

/**
 * 泛型请求类（包含请求data）<br/>
 * 
 * Request&lt;T&gt; request = Request.data(T data);<br/>
 * 
 * default sid = System.currentTimeMillis()
 * 
 * @author zollty
 * @since 2015/9/14
 */
public class Request<T> extends BaseRequest {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * 请求数据，可为基本类型（包装类），可以为其它可序列化对象
     */
    private T data;

    public Request() {
    }

    public Request(T data) {
        this.data = data;
    }

    public Request(T data, String sid) {
        this.data = data;
        this.setSid(sid);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // ～～～～～～～～～～～～～以下是链式编程方法，用于构造Request对象
    /**
     * New一个 T 类型的Request对象
     * 
     * @return Request&ltT&gt对象
     */
    public static <T> Request<T> data() {
        Request<T> result = new Request<T>();
        result.setSid(String.valueOf(System.currentTimeMillis()));
        return result;
    }

    public static <T> Request<T> data(T data) {
        Request<T> result = new Request<T>();
        result.setSid(String.valueOf(System.currentTimeMillis()));
        result.data = data;
        return result;
    }
    
    public Request<T> sid(String sid) {
        this.setSid(sid);
        return this;
    }

}