package org.jretty.apibase.dto;

/**
 * 泛型请求类（包含请求data）<br/>
 * 
 * Request&lt;T&gt; request = Request.create();<br/>
 * 
 * @author zollty
 * @since 2015/9/14
 */
public class Request<T> extends BaseRequest {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * 请求数据，可为基本类型（包装类），可以为其它可序列化对象
     */
    private T data;

    public Request() {
    }

    public Request(T data) {
        this.data = data;
    }

    public Request(T data, String sid) {
        this.data = data;
        this.setSid(sid);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // ～～～～～～～～～～～～～以下是链式编程方法，用于构造Request对象

    /**
     * New一个 T 类型的Request对象
     * 
     * @param t 类型的class
     * @return Request&ltT&gt对象
     */
    public static <T> Request<T> create(Class<T> t) {
        Request<T> result = new Request<T>();
        return result;
    }

    /**
     * New一个 T 类型的Request对象
     * 
     * @return Request&ltT&gt对象
     */
    public static <T> Request<T> create() {
        Request<T> result = new Request<T>();
        return result;
    }

    public Request<T> sid(String sid) {
        this.setSid(sid);
        return this;
    }

    public Request<T> data(T data) {
        this.data = data;
        return this;
    }

}