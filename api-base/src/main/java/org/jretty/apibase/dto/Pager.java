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
 * Create by ZollTy on 2015-9-20 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.apibase.dto;

import java.io.Serializable;

/**
 * 分页请求
 * 
 * @author zollty
 * @since 2015/9/20
 */
public class Pager implements Serializable {
    private static final long serialVersionUID = 2615438537241776166L;
    
    public static final int DEFAULT_PAGE_NUM = 1;
    public static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 页号，从1开始
     */
    private Integer pageNum = DEFAULT_PAGE_NUM;
    /**
     * 每页的大小
     */
    private Integer pageSize = DEFAULT_PAGE_SIZE;

    /**
     * 是否获取总数
     */
    private boolean count = false;

    public Pager() {
    }

    public Pager(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Pager(Integer pageNum, Integer pageSize, boolean count) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.count = count;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        if(pageNum == null){
            throw new IllegalArgumentException("pageNum is null");
        }
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if(pageSize == null){
            throw new IllegalArgumentException("pageSize is null");
        }
        this.pageSize = pageSize;
    }

    public boolean isCount() {
        return count;
    }

    public void setCount(boolean count) {
        this.count = count;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Pager{");
        sb.append("pageNum=").append(pageNum);
        sb.append(", pageSize=").append(pageSize);
        sb.append('}');
        return sb.toString();
    }
}
