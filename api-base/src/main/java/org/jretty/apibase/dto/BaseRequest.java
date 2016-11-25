package org.jretty.apibase.dto;

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