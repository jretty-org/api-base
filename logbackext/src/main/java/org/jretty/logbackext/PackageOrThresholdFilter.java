package org.jretty.logbackext;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class PackageOrThresholdFilter extends Filter<ILoggingEvent> {

    String prefix;

    Level level;

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }

        if (event.getLevel().isGreaterOrEqual(level) || event.getLoggerName().startsWith(prefix)) {
            return FilterReply.NEUTRAL;
        } else {
            return FilterReply.DENY;
        }
    }

    public void start() {
        if (this.prefix != null || this.level != null) {
            super.start();
        }
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setLevel(String level) {
        this.level = Level.toLevel(level);
    }

}