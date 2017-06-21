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
 * Create by ZollTy on 2017-6-6 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.apibase.dto;

/**
 * 包含 code + description 的消息，且可以根据 Locale 获取不同的语言版本的description
 * 
 * @author zollty
 * @since 2017/6/6
 */
public interface IMsg {

    /** 
     * Get the code.
     */
    public String getCode();
    
    /** 
     * Get the msg using default strategy.
     */
    public String getMsg();
    
    /** 
     * Get the msg using the designated locale.
     * @param locale such as zh_CN, en_US...
     */
    public String getMsg(String locale);
    
    /** 
     * Get the code + msg description using the designated locale.
     * @param locale such as zh_CN, en_US...
     */
    public String toString(String locale);
    
}
