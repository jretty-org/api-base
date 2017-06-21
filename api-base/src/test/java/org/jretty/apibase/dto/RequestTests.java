package org.jretty.apibase.dto;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class RequestTests {
    
    @Test
    public void testRequest1() {
        Request<?> ret = Request.data(1024L);

        String json = JSON.toJSONString(ret);

        System.out.println(json);

        Assert.assertTrue(json.contains("\"data\":1024"));
    }

    @Test
    public void testRequest() {
        Request<?> ret = Request.data(1024L).sid("0x0001");

        String json = JSON.toJSONString(ret);

        System.out.println(json);

        Assert.assertEquals("{\"data\":1024,\"sid\":\"0x0001\"}", json);
    }
    
}
