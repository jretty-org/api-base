package org.jretty.apibase.dto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class ResultTests {

    @Test
    public void testSuccess() {
        
        Result<?> ret = Result.success();
        
        Assert.assertTrue(ret.isFailedOrEmpty());

        String json = JSON.toJSONString(ret);
        //System.out.println(json);
        Assert.assertTrue(json.contains("\"success\":true"));
    }
    
    @Test
    public void testData1() {
        User u = new User();
        u.setAccount("fssds");
        u.setId(1003L);
        u.setPassword("48235454");
        u.setUserName("root");
        
        Result<User> ret = Result.success(u);
        
        Assert.assertFalse(ret.isFailedOrEmpty());

        String json = JSON.toJSONString(ret);
        //System.out.println(json);
        Assert.assertTrue(json.contains("\"success\":true"));
        Assert.assertTrue(json.contains("\"data\":{\"account\":\"fssds\",\"id\":1003,\"password\":\"48235454\""));
    }
    
    @Test
    public void testData2() {
        
        List<String> menuList = Arrays.asList("aaa", "bbb", "cccc");
        
        Result<?> ret = Result.success().put("user", "admin").put("menuList", menuList);
        
        Assert.assertFalse(ret.isFailedOrEmpty());

        String json = JSON.toJSONString(ret);
        //System.out.println(json);
        Assert.assertTrue(json.contains("\"success\":true"));
        Assert.assertTrue(json.contains("user"));
        Assert.assertTrue(json.contains("aaa"));
    }
    
    @Test
    public void testData3() {
        List<String> menuList = Arrays.asList("aaa", "bbb", "cccc");
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user", "admin");
        map.put("menuList", menuList);
        
        Result<?> ret = Result.success(map);
        
        Assert.assertFalse(ret.isFailedOrEmpty());

        String json = JSON.toJSONString(ret);
        //System.out.println(json);
        Assert.assertTrue(json.contains("\"success\":true"));
        Assert.assertTrue(json.contains("user"));
        Assert.assertTrue(json.contains("aaa"));
    }
    
    @Test
    public void tests() {
        
        Result<?> ret = Result.fail(Msg.EXCEPTION);
        ret.setLocale(Locale.US.toString());
        
        String json = JSON.toJSONString(ret);
        
        //System.out.println(json);
        Assert.assertTrue(json.contains("System error."));
    }

    @Test
    public void testFail() {
        Result<?> ret = Result.fail("0x0001", "data can not be null.");
        String json = JSON.toJSONString(ret);
        
        //System.out.println(json);

        Assert.assertTrue(json.contains("\"success\":false"));
        Assert.assertTrue(json.contains("\"code\":\"0x0001\""));
        Assert.assertTrue(json.contains("\"msg\":\"data can not be null.\""));
    }
    
    
}
