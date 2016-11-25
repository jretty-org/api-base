package org.jretty.apibase.dto;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

/**
 * 泛型结果类（包含返回data）<br/>
 * 
 * Result&lt;T&gt; result = Result.create();<br/>
 * ...<br/>
 * return result.success();<br/>
 * or <br/>
 * return result.success(data);<br/>
 * or <br/>
 * return result.fail("SomeErrorCode","SomeDescription")<br/>
 * or <br/>
 * return result.fail("SomeErrorCode") <br/>
 * <br/>
 * 
 * or you can do chained callings like below:<br/><br/>
 *
 * result.data(data).code("SomeCode").description("SomeDescription").success(); <br/>
 * 
 * @author zollty
 * @since 2015/9/14
 */
public class Result<T> extends BaseResult {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 返回数据，可为基本类型（包装类），可以为其它可序列化对象
     */
    private T data;
    
    public Result() {
    }

    public Result(T data) {
        this.data = data;
    }

    public Result(T data, String sid) {
        this.data = data;
        this.setSid(sid);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 失败或者返回数据为空
     * 
     * @return
     */
    public boolean failedOrDataEmpty() {
        return !isSuccess() || dataEmpty();
    }

    /**
     * 数据是否为空<br/>
     * 如果是对象,则判断是否为空（NULL）<br/>
     * 如果是字符串,则判断是为空串（NULL,""）<br/>
     * 如果是Collection(List等)对象,则判断是否为空列表（isEmpty)<br/>
     * 如果是Map对象,则判断是否为空Map(isEmpty)<br/>
     * 如果是Array数组，则判断是否为空数组(length == 0)
     * 
     * @return TRUE if data is null or empty
     */
    public boolean dataEmpty() {
        if (data == null) {
            return true;
        }

        if (data instanceof String) {
            String str = (String) data;
            return "".equals(str.trim());
        } else if (data instanceof Collection) {
            Collection<?> list = (Collection<?>) data;
            return list.isEmpty();
        } else if (data instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) data;
            return map.isEmpty();
        } else if (data instanceof Object[]) {
            Object[] array = (Object[]) data;
            return array.length == 0;
        } else if (data instanceof Enumeration) {
            Enumeration<?> enu = (Enumeration<?>) data;
            return enu.hasMoreElements();
        }
        return false;
    }

    // ～～～～～～～～～～～～～以下是链式编程方法，用于构造Result对象

    /**
     * New一个 T 类型的Result，默认success为false
     */
    public static <T> Result<T> create(Class<T> t) {
        Result<T> result = new Result<T>();
        result.setSuccess(false);
        return result;
    }

    /**
     * New一个 T 类型的Result，默认success为false
     */
    public static <T> Result<T> create() {
        Result<T> result = new Result<T>();
        result.setSuccess(false);
        return result;
    }

    public Result<T> success() {
        success(null);
        return this;
    }

    public Result<T> success(T data) {
        this.setSuccess(true);
        this.data = data;
        return this;
    }

    public Result<T> fail(String code, String description) {
        this.setSuccess(false);
        this.setCode(code);
        this.setDescription(description);
        return this;
    }

    public Result<T> fail(BaseResult baseResult) {
        fail(baseResult.getCode(), baseResult.getDescription());
        return this;
    }

    public Result<T> fail(String code) {
        fail(code, null);
        return this;
    }

    public Result<T> code(String code) {
        this.setCode(code);
        return this;
    }

    public Result<T> description(String description) {
        this.setDescription(description);
        return this;
    }

    public Result<T> sid(String sid) {
        this.setSid(sid);
        return this;
    }

    public Result<T> data(T data) {
        this.data = data;
        return this;
    }
}