package org.jretty.logbackext;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

/**
 * 定时推送数据<br>
 * 
 * 可以实现 {@link #handleLog(String)} 方法处理简单日志内容。 <br>
 * 也可以重载 {@link #append(ILoggingEvent)} 方法处理复杂日志内容。
 * 
 * @author zollty
 * @since 2016-9-3
 */
public abstract class TimingAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    
    private ScheduledExecutorService scheduledExecutorService;

    private long delay;

    /**
     * 处理日志内容
     * @param log 日志内容
     */
    protected abstract void handleLog(String log);

    protected abstract Runnable getRunnable();

    @Override
    protected void append(ILoggingEvent event) {
        handleLog(event.getFormattedMessage());
    }

    @Override
    public void start() {
        super.start();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("TimingAppenderScheduledThread");
                return t;
            }
        });
        long initialDelay = delay == 60000 ? calcDelayTime() : 1000;
        System.out.println(String.format("ScheduledExecutorService start to run. initialDelay=%dms, delay=%dms", initialDelay, delay));
        scheduledExecutorService.scheduleWithFixedDelay(getRunnable(), initialDelay, delay, TimeUnit.MILLISECONDS);
    }

    public void setDelay(String delay) {
        this.delay = Long.valueOf(delay);
    }

    /**
     * 获取距当前时间的下一分钟的毫秒数，例如 当前时间（15:21 32S），那么return （15:22 00S） - （15:21 32S） = 28000MS
     */
    private static long calcDelayTime() {
        Date d1 = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d1);
        calendar.set(calendar.get(1), calendar.get(2), calendar.get(5), calendar.get(11), calendar.get(12), 60);
        return calendar.getTime().getTime() - d1.getTime();
    }
    
    @Override
    public void stop() {
      if (!isStarted())
        return;

      // mark this appender as stopped so that Worker can also processPriorToRemoval if it is invoking aii.appendLoopOnAppenders
      // and sub-appenders consume the interruption
      super.stop();

      scheduledExecutorService.shutdown();
      System.out.println("ScheduledExecutorService is stopped.");
    }
}