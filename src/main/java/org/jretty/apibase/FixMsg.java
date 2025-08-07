/* 
 * Copyright (C) 2015-2025 the original author or authors.
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
package org.jretty.apibase;

/**
 * 指定code和msg
 * 
 * @author zollty
 * @since 2017/6/7
 */
public class FixMsg implements IMsg {
    
    private String code;
    
    private String message;
    
    public FixMsg(String code) {
        super();
        this.code = code;
    }
    
    public FixMsg(String code, String message) {
        super();
        this.message = message;
        this.code = code;
    }
    
    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMessage(String locale) {
        return message;
    }

    public void setMessage(String msg) {
        this.message = message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString(String locale) {
        return code + ": " + message;
    }
}
