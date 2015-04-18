/**
 * Project Name:mapreduce
 * File Name:MapReduceUtil.java
 * Package Name:com.hdsx.taxi.upa.mapreduce.utils
 * Date:2014年12月1日上午11:32:33
 * Copyright (c) 2014, sid Jenkins All Rights Reserved.
 * 
 *
*/

package com.hdsx.taxi.upa.mapreduce.utils;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.mongo.MongodbUtil;
import com.hdsx.taxi.mongo.utils.DataUtils;
import com.hdsx.taxi.mongo.utils.LogUtil;
import com.mongodb.DBObject;
import com.mongodb.MapReduceOutput;

/**
 * ClassName:MapReduceUtil
 * Date:     2014年12月1日 上午11:32:33 
 * @author   sid
 * @see 	 
 */
public class MapReduceUtil {
	private static final Logger logger = LoggerFactory.getLogger(MapReduceUtil.class);

	/**
	 * 营收的MapReduce方法
	 *
	 * @author sid
	 * @param code
	 * @param year
	 * @param start
	 * @param end
	 */
	public static void yyMapReduce(String code,String year,String start,String end,String before) {
		//0.判断库是否存在
		String database = MapReduceConfig.getYyPrefix()+"_"+code+"_"+year;
		if (MongodbUtil.getInstance().isExistDB(database)) {
			logger.info("{}数据处理：{}----{}",code,start,end);
			String key = code+"_"+end;
			//1.mapreduce
			HashMap<String, HashMap<String, Object>> hashMap = new HashMap<String, HashMap<String, Object>>();
			HashMap<String, Object> scsj = new HashMap<String, Object>();
			scsj.put("$gt", Long.parseLong(before));
			scsj.put("$lte", Long.parseLong(end));
			hashMap.put("scsj", scsj);//上车时间最早为开始时间的前一天
			HashMap<String, Object> xcsj = new HashMap<String, Object>();
			xcsj.put("$gt", Long.parseLong(start));
			xcsj.put("$lte", Long.parseLong(end));
			hashMap.put("xcsj", xcsj);//下车时间应该为当前锁统计的时间段内的
			
			String mapF = MapReduceConfig.getYyMapF();
			String reduceF = MapReduceConfig.getYReduceF();
			mapF = mapF.replaceAll("@key", key);
			reduceF = reduceF.replaceAll("@key", key);
			mapF = mapF.replaceAll("@timestamp", end);
			reduceF = reduceF.replaceAll("@timestamp", end);
			
			MapReduceOutput out = MongodbUtil.getInstance().mapReduce(database, 
					MapReduceConfig.getYyTb(), mapF, reduceF,MapReduceConfig.getYyInterval(),hashMap);
			for (DBObject o : out.results()) {  
				LogUtil.getInstance().getLogger(MapReduceUtil.class).info(o.toString());
			} 
			
			//2.插入结果集
			Iterable<DBObject> results = out.results();
			if (DataUtils.isNotEmpty(results)) {
				for (DBObject dbObject : results) {
					MongodbUtil.getInstance().insertMapReduceResult(MapReduceConfig.getYyMapreduce(), 
							MapReduceConfig.getYyInterval(), dbObject);
				}
			}else{
				logger.info("{}时间段：{}----{},数据为空暂不处理",code,start,end);
				return;
			}
			
			logger.info("{}数据处理完成：{}----{}",code,start,end);
		}else{

			logger.info("{}数据库不存在,处理时间段：{}----{}",code,start,end);
		}
	}
}
