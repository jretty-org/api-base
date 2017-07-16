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
 * Create by ZollTy on 2017-6-7 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.apibase.dto;

/**
 * 指定code和msg
 * 
 * @author zollty
 * @since 2017/6/7
 */
public class FixMsg implements IMsg {
    
    private String code;
    
    private String msg;
    
    public FixMsg(String code) {
        super();
        this.code = code;
    }
    
    public FixMsg(String code, String msg) {
        super();
        this.msg = msg;
        this.code = code;
    }
    
    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String getMsg(String locale) {
        return msg;
    }

    @Override
    public String toString(String locale) {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
