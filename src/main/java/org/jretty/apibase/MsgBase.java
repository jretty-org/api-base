/*
 * Copyright (C) 2018-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2018-9 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.apibase;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目统一的响应代码、错误代码定义
 * <p>
 * 每一条信息应该包括：代码和注释。</p>
 *    
 * <p>
 * 特别提醒： 一定要写注释，方便自己和别人快速查看！ 代码格式请保持统一！</p>
 * 
 * <p>
 * CODE定义风格自由，一般有三种方式：
 * 
 * <pre>{@code

风格1：眯着眼睛随便敲4~6个随机字母/数字：
    KFDJG1("无效ticket：{}")
    FGD08F("菜单URL不能为空")

风格2：英文或拼音大写字母
    PASSWD_INVALID("手机号或密码错误"),
    TOKEN_INVALID("token失效，请重新登录"),

风格3：自定义数字
    QX_EMPTY_CERID("100701", "证件号码为空"),
    QX_EMPTY_NAME("100702", "名称为空"),

 * }</pre>
 * 
 * <p>第一种写代码的人最爽，第二种看代码的人最爽，第三种只为了适配某些特殊规范。
 * 
 * <pre>{@code

每个CODE建议`只使用一次，不要在多处代码使用`。 
当有多处需要使用相同信息时，可以引用，例如：
    KFDJG1("无效ticket：{}"),
    KFDJG2(KFDJG1.getMsg()),
    KFDJG3(KFDJG1.getMsg()),
    KFDJG4(KFDJG1.getMsg()),
遵循这个建议会让你在根据错误信息排查程序逻辑时提供最好的帮助。
例如，客户告诉你，她登录报错：“code = KFDJG3， msg = 无效ticket：a9e8d98c9e9d”
因为每个code在代码中只有一处使用，所以根据code = KFDJG3即可直接定位到报错的代码行！！
 * }</pre>
 *
 * <p>注意，这里只解决了信息的定义及代码关联问题。没有解决运行时错误日志及上下文定位问题。
 * <p>
 * <p>还需要结合异常及日志的处理设计，当遇到未知异常时可以生成服务器唯一编码，例如imYn2e-051310381。
 * <p>因为是全局唯一的，所以直接搜索日志就能定位到准确的日志位置。
 *
 * <p>MsgBase类定义了常见的异常类型，分类如下：
 * <pre>
 * 1、参数错误（Parameter Error）：参数无效、空值、格式错误等。
 *
 * 2、状态错误（State Error）：对象状态不符合操作要求。
 *
 * 3、权限错误（Permission Error）：用户权限不足。
 *
 * 4、业务错误（Business Error）：违反业务规则，包括：
 * 数据不存在错误（Data Not Found Error）
 * 数据一致性错误（Data Consistency Error）
 * 业务规则错误（Business Rule Error）
 *
 * 5、系统错误（System Error）：系统级故障，如数据库连接问题。
 * </pre>
 *
 * @author zollty
 * 
 */
public enum MsgBase implements IMsg {
    
    // -- 公用的、常见的错误类型
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /** 操作成功 */
    SUCCESS("200", "操作成功", "Success.", "操作成功"),
    
    /** 操作失败 */
    FAILED("500", "操作失败", "Failed.", "操作失敗"),

    /** 参数错误 {} */
    PARAM_INVALID("B0100", "参数错误 {}", "Param Error {}", "無效的參數：{}"),
    /** 参数{}不能为空！ */
    PARAM_EMPTY("B0102", "参数`{}`不能为空！", "Arguments `{}` can not be empty!", "參數`{}`不能為空！"),

    /** 状态错误 {} */
    STATE_ERROR("B0200", "状态错误 {}", "State Error {}"),

    /** 权限错误 {} */
    PERMISSION_DENIED("B0300", "权限错误 {}", "Permission Denied {}"),

    /** 业务错误 {} */
    BUSINESS_ERROR("B0400", "业务错误 {}", "Business Error {}"),
    /** 数据错误 */
    DATA_ERROR("B0401", "数据错误 {}", "Data Error {}"),
    /** {}数据不存在 */
    DATA_NOT_FOUND("B0402", "`{}`数据不存在", "`{}` Data Not Found"),

    /** 系统异常 {} */
    SYSTEM_ERROR("B0500", "系统异常 {}", "System Error {}", "系統異常 {}"),

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** 【@Deprecated 不推荐使用】 系统异常（省略具体信息） */
    OMIT_SYSTEM_ERR("B0000", "系统异常", "System Error", "系統異常"),

    /** 【@Deprecated 不推荐使用】 未知错误，参见：{} */
    UNKNOWN_ERR("B0001", "未知错误，参见：{}", "Unexpected error. See: {}", "未知錯誤，參見：{}"),

    /** 【@Deprecated 不推荐使用】 未定义错误，参见：{} */
    UNDEFINED_ERR("B0002", "未定义错误，参见：{}", "Undefined error. See: {}", "未定義錯誤，參見：{}"),
    
    // -- 其他自定义的错误类型
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    // DB开头 -- 数据库相关的错误信息
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // USR开头 -- User相关的错误信息
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    // TOKEN开头 -- Token相关的错误信息
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    // MENU开头 -- Menu相关的错误信息
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    ;

    private static final boolean USE_NAME_AS_CODE_DEFAULT = false;
    
    private final String msgZhCn;
    
    private String msgEnUs;
    
    private String msgZhTw;
    
    private final String code;

    static {
        MsgCodeHolder.addMsg(MsgBase.class);
    }
    
    /**
     * 构造函数
     */
    private MsgBase(String msgZhCn) {
        this.msgZhCn = msgZhCn;
        if (USE_NAME_AS_CODE_DEFAULT) {
            this.code = this.name();
        } else {
            this.code = newCode(this.name(), msgZhCn);
        }
    }

    private MsgBase(String code, String msgZhCn) {
        this.code = code;
        this.msgZhCn = msgZhCn;
    }

    private MsgBase(String code, String msgZhCn, String msgEnUs) {
        this.code = code;
        this.msgZhCn = msgZhCn;
        this.msgEnUs = msgEnUs;
    }

    private MsgBase(String code, String msgZhCn, String msgEnUs, String msgZhTw) {
        this.code = code;
        this.msgZhCn = msgZhCn;
        this.msgEnUs = msgEnUs;
        this.msgZhTw = msgZhTw;
    }

    private MsgBase(int useNameAsCode, String msgZhCn) {
        this.msgZhCn = msgZhCn;
        if (useNameAsCode == 1) {
            this.code = this.name();
        } else {
            this.code = newCode(this.name(), msgZhCn);
        }
    }

    private MsgBase(int useNameAsCode, String msgZhCn, String msgEnUs) {
        if (useNameAsCode == 1) {
            this.code = this.name();
        } else {
            this.code = newCode(this.name(), msgZhCn);
        }
        this.msgZhCn = msgZhCn;
        this.msgEnUs = msgEnUs;
    }

    private MsgBase(int useNameAsCode, String msgZhCn, String msgEnUs, String msgZhTw) {
        if (useNameAsCode == 1) {
            this.code = this.name();
        } else {
            this.code = newCode(this.name(), msgZhCn);
        }
        this.msgZhCn = msgZhCn;
        this.msgEnUs = msgEnUs;
        this.msgZhTw = msgZhTw;
    }

    /**
     * @return Returns the code.
     */
    @Override
    public String getCode() {
        return code;
    }
    
    @Override
    public String getMessage() {
        if (msgZhCn != null) {
            return msgZhCn;
        }
        if (msgEnUs != null) {
            return msgEnUs;
        }
        if (msgZhTw != null) {
            return msgZhTw;
        }
        return "";
    }
    
    @Override
    public String getMessage(String locale) {
        if ("zh_CN".equals(locale)) {
            return msgZhCn;
        }

        if ("en_US".equals(locale)) {
            return msgEnUs;
        }

        if ("zh_TW".equals(locale)) {
            return msgZhTw;
        }

        return getMessage();
    }
    
    @Override
    public String toString(){
        return code + "=[" + this.name() + "] " + getMessage();
    }
    
    @Override
    public String toString(String local){
        return code + "=[" + this.name() + "] " + getMessage(local);
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     */
    public static MsgBase getByCode(String code) {
        for (MsgBase msg : values()) {
            if (msg.getCode().equals(code)) {
                return msg;
            }
        }
        return null;
    }

    /**
     * 获取全部枚举
     */
    public static List<MsgBase> getAllEnum() {
        List<MsgBase> list = new ArrayList<MsgBase>();
        for (MsgBase msg : values()) {
            list.add(msg);
        }
        return list;
    }

    /**
     * 获取全部枚举值
     */
    public static List<String> getAllEnumCode() {
        List<String> list = new ArrayList<String>();
        for (MsgBase msg : values()) {
            list.add(msg.getCode());
        }
        return list;
    }
    
    
    /**
     * code的特点： 可以根据code找到对应的错误信息，且 code 和错误信息是一一对应关系。
     */
    private static String newCode(String name, String errorMsg) {
        // shorMsg算法
        return InnerUtils.shortMsg(name + errorMsg);
    }

}

