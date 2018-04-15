package org.jretty.id;

/**
 * Generate Unique ID
 * 
 * @author zollty
 * @since 2017-11-15
 */
public interface IdGenerator {
    
    /**
     * 获取ID
     * @return ID
     */
    long nextId();

}
