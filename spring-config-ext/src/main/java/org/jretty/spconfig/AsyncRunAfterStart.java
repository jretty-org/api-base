package org.jretty.spconfig;

/**
 * 在spring bean容器加载完成后（启动后） 异步 执行doInit(args)方法，
 * See {@link SpringRefreshedListener}. 
 * 
 * @author zollty
 */
public interface AsyncRunAfterStart extends RunAfterStart {

}
