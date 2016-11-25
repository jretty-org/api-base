package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackTests {
    
    final static Logger logger = LoggerFactory.getLogger(LogbackTests.class);
    
    public static void main(String[] args) {
        
        logger.trace("trace");
        logger.debug("debug str");
        logger.info("info str");
        logger.warn("warn");
        logger.error("error");
        
    }

}
