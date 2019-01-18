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

import java.io.Serializable;

/**
 * 基础请求类
 * 
 * @author zollty
 * @since 2015/9/14
 */
public class BaseRequest implements Serializable {
    
    private static final long serialVersionUID = 3340252857601228483L;
    
    /**
     * 请求标识号
     */
    private String sid;

    /**
     * @return the sid
     */
    public String getSid() {
        return sid;
    }

    /**
     * @param sid
     *            the sid to set
     */
    public void setSid(String sid) {
        this.sid = sid;
    }
}