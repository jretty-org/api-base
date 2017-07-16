package org.jretty.apibase;

import org.jretty.apibase.IMsg;

/**
 * 项目统一的异常代码定义
 * <p>
 * ReadMe：
 * ----每一条错误信息应该包括：错误代码和注释。
 * <p>
 * ----错误代码（code）命名规范如下：
 * 总长度在30以内，易于区别错误所属的模块(或类型)，字母后面可以跟3~4个数字
 * <p>
 * ----特别提醒：
 * 
 *  一定要写注释，方便自己和别人快速查看！ 代码格式请保持统一！
 */
public enum Msg implements IMsg {
    
    // -- 公用的、常见的错误类型
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /** 操作成功 */
    SUCCESS("200", "操作成功", "Success.", "操作成功"),
    
    /** 操作失败 */
    FAILED("EC0001", "操作失败", "Failed.", "操作失敗"),
    
    /** 系统异常 */
    EXCEPTION("EC0002", "系统异常", "System error.", "系統異常"),
    
    /** 未知错误，参见：{} */
    UNKNOWN_ERR("EC0003", "未知错误，参见：{}", "Unexpected error. See: {}", "未知錯誤，參見：{}"),
    
    /** 未定义错误，参见：{} */
    UNDEFINED_ERR("EC0004", "未定义错误，参见：{}", "Undefined error. See: {}", "未定義錯誤，參見：{}"),
    
    /** 无效的参数：{} */
    PARAM_INVALID("EC0005", "无效的参数：{}", "Illegal arguments: {}", "無效的參數：{}"),
    
    /** 参数不能为空：{} */
    PARAM_EMPTY("EC0006", "参数不能为空：{}", "Arguments can not be empty: {}", "參數不能為空：{}"),
    
    // MENU开头 -- Menu相关的错误信息
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /** 菜单名称不能为空 */
    MENU_NAME_EMPTY("菜单名称不能为空"),
    /** 上级菜单不能为空 */
    MENU_PARENT_EMPTY("上级菜单不能为空", "parent menu can not be empty!", null),
    /** 菜单URL不能为空 */
    MENU_URL_EMPTY("菜单URL不能为空"),
    /** 上级菜单只能为目录类型 */
    MENU_PARENT_ONLY_BE_DIR("上级菜单只能为目录类型"),
    /** 上级菜单只能为菜单类型 */
    MENU_PARENT_ONLY_BE_MENU("上级菜单只能为菜单类型"),
    
    ;
    
    private String msgZhCN;
    
    private String msgEnUS;
    
    private String msgZhTW;
    
    private String code;
    
    
    /** 
     * 构造函数
     */
    private Msg(String msgZhCN) {
        this.msgZhCN = msgZhCN;
        this.code = newCode(this.name(), msgZhCN);
    }
    
    /** 
     * 构造函数
     */
    private Msg(String code, String msgZhCN) {
        this.code = code;
        this.msgZhCN = msgZhCN;
    }
    
    /** 
     * 构造函数
     */
    private Msg(String msgZhCN, String msgEnUS, String msgZhTW) {
        this.msgZhCN = msgZhCN;
        this.msgEnUS = msgEnUS;
        this.msgZhTW = msgZhTW;
        this.code = newCode(this.name(), getMsg());
    }
    
    /** 
     * 构造函数
     */
    private Msg(String code, String msgZhCN, String msgEnUS, String msgZhTW) {
        this.code = code;
        this.msgZhCN = msgZhCN;
        this.msgEnUS = msgEnUS;
        this.msgZhTW = msgZhTW;
    }
    
    /**
     * @return Returns the code.
     */
    @Override
    public String getCode() {
        return code;
    }
    
    @Override
    public String getMsg() {
        if (msgZhCN != null) {
            return msgZhCN;
        }
        if (msgEnUS != null) {
            return msgEnUS;
        }
        if (msgZhTW != null) {
            return msgZhTW;
        }
        return "";
    }
    
    @Override
    public String getMsg(String local) {
        if ("zh_CN".equals(local)) {
            return msgZhCN;
        }

        if ("en_US".equals(local)) {
            return msgEnUS;
        }

        if ("zh_TW".equals(local)) {
            return msgZhTW;
        }

        return getMsg();
    }
    
    @Override
    public String toString(){
        return code + "=[" + this.name() + "] " + getMsg();
    }
    
    @Override
    public String toString(String local){
        return code + "=[" + this.name() + "] " + getMsg(local);
    }
    
    private static String newCode(String name, String errorMsg) {
        // shorMsg算法
        return name;
    }
}

