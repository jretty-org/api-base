package org.jretty.logbackext.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LogIdGenerator {
	private static Map<String, String> LogIdMap = new HashMap<String, String>();
	final static DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

	public static String makeDefaultLogId() {
		String threadId = String.valueOf(Thread.currentThread().getId());
		StringBuilder sb = new StringBuilder();
		sb.append(df.format(new Date()));
		sb.append("-");
		sb.append(threadId);
		String logId = sb.toString();
		LogIdMap.put(threadId, logId);
		return logId;
	}

	public static void putCustomLogId(String logId) {
		String threadId = String.valueOf(Thread.currentThread().getId());
		LogIdMap.put(threadId, logId);
	}

	public static String getCurrentLogId() {
		String threadId = String.valueOf(Thread.currentThread().getId());
		String logId = LogIdMap.get(threadId);
		if(logId == null || "".equals(logId)){
			logId = makeDefaultLogId();
		}
		return logId;
	}
}
