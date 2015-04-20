/**
 * Project Name:mapreduce
 * File Name:MapReduceTest.java
 * Package Name:mapreduce.test
 * Date:2015年1月28日下午1:38:41
 * Copyright (c) 2015, sid Jenkins All Rights Reserved.
 * 
 *
 */

package mapreduce.test;

import java.util.HashMap;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.db.mongodb.main.MongodbUtil;
import utils.utils.DataTypeUtil;
import utils.utils.LogUtil;

import com.hdsx.taxi.upa.mapreduce.utils.MapReduceConfig;
import com.hdsx.taxi.upa.mapreduce.utils.MapReduceUtil;
import com.mongodb.DBObject;
import com.mongodb.MapReduceOutput;

/**
 * ClassName:MapReduceTest Function: TODO ADD FUNCTION. Reason: TODO ADD REASON.
 * Date: 2015年1月28日 下午1:38:41
 * 
 * @author sid
 * @see
 */
public class MapReduceTest extends TestCase {
	private static final Logger logger = LoggerFactory
			.getLogger(MapReduceTest.class);

	public void testMapReduce() {
		// String code = "500100";
		// String year="2015";
		// String start="20150128090000";
		// String end="20150128103000";
		// String before="20150127090000";
		// //0.判断库是否存在
		// String database = MapReduceConfig.getYyPrefix()+"_"+code+"_"+year;
		// if (MongodbUtil.getInstance().isExistDB(database)) {
		// logger.info("{}数据处理：{}----{}",code,start,end);
		// String key = code+"_"+end;
		// //1.mapreduce
		// HashMap<String, HashMap<String, Object>> hashMap = new
		// HashMap<String, HashMap<String, Object>>();
		// HashMap<String, Object> scsj = new HashMap<String, Object>();
		// scsj.put("$gt", before);
		// scsj.put("$lte", end);
		// hashMap.put("n05", scsj);//上车时间最早为开始时间的前一天
		// HashMap<String, Object> xcsj = new HashMap<String, Object>();
		// xcsj.put("$gt", start);
		// xcsj.put("$lte", end);
		// hashMap.put("n07", xcsj);//下车时间应该为当前锁统计的时间段内的
		//
		// String mapF = MapReduceConfig.getYyMapF();
		// String reduceF = MapReduceConfig.getYReduceF();
		// mapF = mapF.replaceAll("@key", key);
		// reduceF = reduceF.replaceAll("@key", key);
		// mapF = mapF.replaceAll("@timestamp", end);
		// reduceF = reduceF.replaceAll("@timestamp", end);
		//
		// MapReduceOutput out = MongodbUtil.getInstance().mapReduce(database,
		// MapReduceConfig.getYyTb(), mapF,
		// reduceF,MapReduceConfig.getYyInterval(),hashMap);
		// for (DBObject o : out.results()) {
		// LogUtil.getInstance().getLogger(MapReduceUtil.class).info(o.toString());
		// }
		//
		// //2.插入结果集
		// Iterable<DBObject> results = out.results();
		// if (DataUtils.isNotEmpty(results)) {
		// System.out.println(results);
		// }else{
		// logger.info("{}时间段：{}----{},数据为空暂不处理",code,start,end);
		// return;
		// }
		//
		// //3.删除原表中的MapReduce结果集
		// HashMap<String, Object> map = new HashMap<String, Object>();
		// map.put("_id", key);
		// MongodbUtil.getInstance().remove(database,
		// MapReduceConfig.getYyInterval(),map);
		//
		// logger.info("{}数据处理完成：{}----{}",code,start,end);
		// }else{
		//
		// logger.info("{}数据库不存在,处理时间段：{}----{}",code,start,end);
		// }
	}

	public void testShardMapReduce() {
		String code = "321200";
		String year = "2015";
		String start = "20150205090000";
		String end = "201502050930000";
		String before = "20150204090000";
		// 0.判断库是否存在
		String database = MapReduceConfig.getYyPrefix() + "_" + code + "_"
				+ year;
		if (MongodbUtil.getInstance().isExistDB(database)) {
			logger.info("{}数据处理：{}----{}", code, start, end);
			String key = code + "_" + end;
			// 1.mapreduce
			HashMap<String, HashMap<String, Object>> hashMap = new HashMap<String, HashMap<String, Object>>();
			HashMap<String, Object> scsj = new HashMap<String, Object>();
			scsj.put("$gt", before);
			scsj.put("$lte", end);
			hashMap.put("n05", scsj);// 上车时间最早为开始时间的前一天
			HashMap<String, Object> xcsj = new HashMap<String, Object>();
			xcsj.put("$gt", start);
			xcsj.put("$lte", end);
			hashMap.put("n07", xcsj);// 下车时间应该为当前锁统计的时间段内的

			String mapF = MapReduceConfig.getYyMapF();
			String reduceF = MapReduceConfig.getYReduceF();
			mapF = mapF.replaceAll("@key", key);
			reduceF = reduceF.replaceAll("@key", key);
			mapF = mapF.replaceAll("@timestamp", end);
			reduceF = reduceF.replaceAll("@timestamp", end);
			
			MapReduceOutput out = MongodbUtil.getInstance().mapReduce(database,
					MapReduceConfig.getYyTb(), mapF, reduceF,
					MapReduceConfig.getYyInterval(), hashMap);
			for (DBObject o : out.results()) {
				LogUtil.getInstance().getLogger(MapReduceUtil.class)
						.info(o.toString());
			}

			// 2.插入结果集
			Iterable<DBObject> results = out.results();
			if (DataTypeUtil.isNotEmpty(results)) {
				System.out.println(results);
			} else {
				logger.info("{}时间段：{}----{},数据为空暂不处理", code, start, end);
				return;
			}

			logger.info("{}数据处理完成：{}----{}", code, start, end);
		} else {

			logger.info("{}数据库不存在,处理时间段：{}----{}", code, start, end);
		}
	}
}
