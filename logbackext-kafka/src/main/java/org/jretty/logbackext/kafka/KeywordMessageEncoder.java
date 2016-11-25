package org.jretty.logbackext.kafka;

import java.io.UnsupportedEncodingException;

import kafka.utils.VerifiableProperties;

/**
 * 自定义消息编码器
 * 
 * @author kevin
 *
 */
public class KeywordMessageEncoder implements kafka.serializer.Encoder<Object> {

	public KeywordMessageEncoder(VerifiableProperties props) {

	}

	public byte[] toBytes(Object obj) {
		byte[] bytes = null;
		try {
			if (obj instanceof String) {
				bytes = ((String) obj).getBytes("utf-8");
			} else if (obj instanceof byte[]) {
				bytes = (byte[]) obj;
			}

		} catch (UnsupportedEncodingException e) {
		}

		return bytes;
	}

}
