package org.jretty.id;

import org.junit.Test;

/**
 * 
 * @author zollty
 * @since 2018-4-11
 */
@SuppressWarnings("unused")
public class SequenceTest {
    
//    @Test
    public void test_getDatacenterId() {
        System.out.println(Sequence.getDatacenterId(31));
    }
    
//    @Test
    public void test_getMaxWorkerId() {
        System.out.println(Sequence.getMaxWorkerId(23, 31));
    }

//    @Test
    public void test_Sequence() {
        System.out.println(new Sequence().nextId());
        System.out.println(new Sequence().nextId());
        System.out.println(new Sequence().nextId());
    }
    
    
}
