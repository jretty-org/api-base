package org.jretty.apibase.dto;

import org.jretty.apibase.dto.Request;
import org.jretty.apibase.dto.Result;
import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class DtoTests {

	@Test
	public void testResult() {
		Result<?> ret = Result.create().code("0x0001").description("data can not be null.");

		String json = JSON.toJSONString(ret);

		System.out.println(json);

		Assert.assertEquals("{\"code\":\"0x0001\",\"description\":\"data can not be null.\",\"success\":false}", json);

	}
	
	
	@Test
	public void testRequest() {
		Request<?> ret = Request.create().sid("0x0001").data(1024L);

		String json = JSON.toJSONString(ret);

		System.out.println(json);

		Assert.assertEquals("{\"data\":1024,\"sid\":\"0x0001\"}", json);

	}

}
