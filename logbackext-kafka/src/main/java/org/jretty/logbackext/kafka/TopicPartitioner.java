package org.jretty.logbackext.kafka;

import java.util.Random;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;
/**
 * 自定义分区实现
 * @author kevin
 *
 */
public class TopicPartitioner implements Partitioner {
	private Random rnd = new Random();
	
	public TopicPartitioner(VerifiableProperties props) {

	}

	/**
	 * 返回分区索引编号
	 * 
	 * @param key
	 *            sendMessage时，输出的partKey
	 * @param numPartitions
	 *            topic中的分区总数
	 * @return
	 */
	public int partition(Object key, int numPartitions) {
		int partition = 0;
		if(key != null){
			Integer partKey = (Integer)key;
			if(partKey.intValue() == 0){
				partition = rnd.nextInt(numPartitions);
			}else{
				partition = Math.abs(partKey) % numPartitions;
			}
			
		}
		return partition;
	}
	

}
