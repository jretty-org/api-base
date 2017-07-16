package org.jretty.apibase.dto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

/**
 * Result类单元测试
 * 
 * @author zollty
 * @since 2015/9/14
 */
public class ResultTests {
    
    // --------------------------用法示例：
    
    // 1. 成功时有data
    public Result<User> query() {
        User data = new User();
        int flag = 1;
        if (flag > 0) {
            return Result.success(data);
        }
        return Result.fail();
    }

    // 2. 成功时无data
    public Result<User> query2() {
        int flag = 1;
        if (flag > 0) {
            return Result.success();
        }
        return Result.fail();
    }
    
    // 3. 成功返回无data，但有map数据
    public Result<Object> query3() {
        int flag = 1;
        if (flag > 0) {
            return Result.success().put(10086L, "admin").put("user", "abc");
        }
        return Result.fail();
    }
    
    // 4. 成功返回无data，但有多段map数据
    public Result<Object> query4() {
        int flag = 1;
        Result<Object> ret = Result.success();
        if (flag > 0) {
            ret.put(10086L, "admin");
        }
        if (flag > 24) {
            ret.put("user", "abc");
        }
        if (flag < -1) {
            return Result.fail();
        }
        return ret;
    }
    
    // 5. 失败时有data
    public Result<User> query5() {
        User data = new User();
        int flag = 1;
        if (flag > 0) {
            return Result.success(data);
        }
        
        Result<User> ret = Result.fail();
        ret.data(data);
        // 或者 return Result.create(data).success(false);
        return ret;
    }
    
    // 6. 成功、失败都有多段map数据
    public Result<Object> query6() {
        int flag = 1;
        Result<Object> ret = Result.create();
        if (flag > 0) {
            ret.put(10086L, "admin");
        }
        if (flag > 24) {
            ret.put("user", "abc");
        }
        
        boolean isSuccess = false;
        
        return ret.success(isSuccess);
    }
    

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
        
        Result<Object> ret = Result.success().put("user", "admin").put("menuList", menuList);
        
        Assert.assertFalse(ret.isFailedOrEmpty());

        String json = JSON.toJSONString(ret);
        // {"menuList":["aaa","bbb","cccc"],"success":true,"timestamp":1500115273653,"user":"admin"}
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
    public void testData4() {
        List<String> menuList = Arrays.asList("aaa", "bbb", "cccc");
        User u = new User();
        u.setAccount("fssds");
        u.setId(1003L);
        u.setPassword("48235454");
        u.setUserName("root");
        
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("4554", "fssds");
        data.put("354", "root");
        data.put("password", "48235454");
        data.put("id", 1003L);
        
        Result<Object> ret = Result.success(data).put("user", "admin").put("menuList", menuList);
        ret.data(u);
        
        Assert.assertFalse(ret.isFailedOrEmpty());
        
        Assert.assertEquals("fssds", ret.get("4554"));
        Assert.assertEquals("48235454", ret.get("password"));

        String json = JSON.toJSONString(ret);
        // System.out.println(json);
        Assert.assertTrue(json.contains("\"success\":true"));
        Assert.assertTrue(json.contains("\"data\":{\"account\":\"fssds\",\"id\":1003,\"password\":\"48235454\""));
    }
    
    @Test
    public void testData5() {
        // 允许map的key为Object，但是不建议这么用
        Result<Object> ret = Result.success().put(10086L, "admin").put("menuList", "sdfsdf");
        
        String json = JSON.toJSONString(ret);
        Assert.assertEquals("admin", ret.get(10086L));
        
        Assert.assertTrue(json.contains("10086:\"admin\""));
    }
    
    @Test
    public void testFail1() {
        
        Result<?> ret = Result.fail();
        
        String json = JSON.toJSONString(ret);
        
        //System.out.println(json);
        Assert.assertTrue(json.contains("500"));
    }
    
    @Test
    public void testFail2() {
        
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
    
    
    @Test
    public void tests() {

        int flag = 1;

        Result<?> ret = Result.create().success(flag > 0);

        String json = JSON.toJSONString(ret);

        Assert.assertTrue(ret.isSuccess());
        Assert.assertTrue(json.contains("\"success\":true"));
    }
    
    
}
