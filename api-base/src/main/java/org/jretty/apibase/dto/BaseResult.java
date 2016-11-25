package org.jretty.apibase.dto;

import java.io.Serializable;

/**
 * 基础回复类
 * 
 * @author zollty
 * @since 2015/9/14
 */
public class BaseResult implements Serializable {

    private static final long serialVersionUID = 2991693728111260232L;

    /**
     * 请求标识号
     */
    private String sid;

    /** 成功标志 */
    private boolean success;

    /** 信息码 */
    private String code;

    /** 描述 */
    private String description;

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

    /**
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @param success
     *            the success to set
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "BaseResult [" + (sid != null ? "sid=" + sid + ", " : "") + "success=" + success + ", "
                + (code != null ? "code=" + code + ", " : "")
                + (description != null ? "description=" + description : "") + "]";
    }

}