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
import java.util.ArrayList;
import java.util.List;

/**
 * 分页数据
 * 
 * @author zollty
 * @since 2015/9/20
 */
public class PagerData<T> extends Pager implements Serializable {
    private static final long serialVersionUID = -5668252650922800349L;
    
    /**
     * 总页数
     */
    private Integer totalPage;
    /**
     * 数据总数，满足条件的数量，非本次查询结果条数， 本次查询的具体结果由dataList字段确定
     */
    private Integer totalCount;
    /**
     * 具体的数据
     */
    private List<T> list = new ArrayList<T>();
    
    
    /**
     * 分页
     * @param list        列表数据
     * @param pageSize    每页记录数
     * @param pageNum     当前页数
     */
    public static <T> PagerData<T> create() {
        return new PagerData<T>();
    }
    
    /**
     * 分页
     * @param list        列表数据
     * @param pageSize    每页记录数
     * @param pageNum     当前页数
     */
    public static <T> PagerData<T> create(List<T> list, int pageSize, int pageNum) {
        return new PagerData<T>(list, pageSize, pageNum);
    }
    
    /**
     * 分页
     * @param list        列表数据
     * @param totalCount  总记录数
     * @param pageSize    每页记录数
     * @param pageNum     当前页数
     */
    public static <T> PagerData<T> create(List<T> list, int pageSize, int pageNum, int totalCount) {
        return new PagerData<T>(list, pageSize, pageNum, totalCount);
    }
    
    private PagerData() {
    }
    
    private PagerData(List<T> list, int pageSize, int pageNum) {
        this.setList(list);
        this.setPageSize(pageSize);
        this.setPageNum(pageNum);
    }
    
    private PagerData(List<T> list, int pageSize, int pageNum, int totalCount) {
        this(list, pageSize, pageNum);
        this.setTotalCount(totalCount);
    }
    
    public Integer getTotalPage() {
        return totalPage;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public List<T> getList() {
        return list;
    }

    public PagerData<T> setTotalCount(Integer totalCount) {
        this.setCount(false);
        this.totalCount = totalCount;
        this.setTotalPage( (int) Math.ceil((double)totalCount /getPageSize()) );
        return this;
    }

    /**
     * 通常不设置，根据 totalCount 和 pageSize自动计算
     */
    public PagerData<T> setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
        return this;
    }

    public PagerData<T> setList(List<T> list) {
        this.list = list;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PagerData [totalPage=");
        builder.append(totalPage);
        builder.append(", totalCount=");
        builder.append(totalCount);
        builder.append(", pageNum=");
        builder.append(getPageNum());
        builder.append(", pageSize=");
        builder.append(getPageSize());
        builder.append(", count=");
        builder.append(isCount());
        builder.append(", list=");
        builder.append(list);
        builder.append("]");
        return builder.toString();
    }

}