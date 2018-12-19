package org.jretty.spconfig;

import org.jretty.util.NamedThreadFactory;
import org.jretty.util.NestedRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 在spring bean 容器加载完成后做一些事情 （执行顺序在<code>@PostConstruct</code>之后）
 * 
 * <p>See {@link #onApplicationEvent}
 * 
 * @author zollty
 * @since 2017-5-31
 */
abstract public class SpringRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    protected final Logger logger = LoggerFactory.getLogger(SpringRefreshedListener.class);
    private NamedThreadFactory fac = new NamedThreadFactory("AsyncRunAfterStart_Thread");
    private boolean loadOnce = true;
    private boolean loaded = false;
    
    // 容器加载完成后做的任务
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(loadOnce && !loaded) {
            loaded = true;
            ApplicationContext ctx = event.getApplicationContext();
            
            doInit(ctx);
        }
    }
    
    /**
     * 调用doInit，执行RunAfterStart接口的实现类，
     *  例如，doInit(ctx, JobService.class);
     */
    abstract protected void doInit(ApplicationContext ctx);
    
    protected void doInit(ApplicationContext ctx, 
            Class<? extends RunAfterStart> cls, final Object... args) {
        final RunAfterStart r = ctx.getBean(cls);
        if(r instanceof AsyncRunAfterStart) {
            fac.newThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        r.doInit(args);
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
            }).start();
        } else {
            try {
                r.doInit(args);
            } catch (Exception e) {
                logger.error("", e);
                // 抛出异常，以关闭服务器
                throw new NestedRuntimeException(e);
            }
        }
    }

    /**
     * @param loadOnce the loadOnce to set
     */
    public void setLoadOnce(boolean loadOnce) {
        this.loadOnce = loadOnce;
    }

}