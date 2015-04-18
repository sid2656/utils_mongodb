/**
 * Project Name:mapreduce
 * File Name:Config.java
 * Package Name:com.hdsx.taxi.upa.mapreduce.utils
 * Date:2014年12月1日上午10:20:22
 * Copyright (c) 2014, sid Jenkins All Rights Reserved.
 * 
 *
*/

package com.hdsx.taxi.upa.mapreduce.utils;
/**
 * ClassName:Config
 * Date:     2014年12月1日 上午10:20:22 
 * @author   sid
 * @see 	 
 */
public class MapReduceConfig {

	private static Integer interval = null;//营收数据循环统计间隔
	private static Integer delay = null;//营收数据统计延迟时间
	private static String yyPrefix = null;//待处理营收数据库前缀
	private static String yyTb = null;//待处理营收数据表名
	private static String dbInfo = null;//基础信息数据库名
	private static String tcpUser = null;//tcpuser基础信息表名
	private static String yyStart = null;//营运统计开始时间
	private static String yyMapreduce = null;//营运reduce统计库名
	private static String yyInterval = null;//营运循环统计表名
	private static String yyMapF = null;//营运map方法
	private static String yyReduceF = null;//营运Reduce方法
	private static String yyVersion = null;//营收MapReduce的版本号
	private static String locationURL = null;//地址服务的url

	/**
	 * 
	 * 营收MapReduce的版本号
	 * @author sid
	 * @return
	 */
	public static String getLocationURL(){
		if (locationURL==null) {
			locationURL = MapReducePropertiesUtil.getProperties().getProperty("location.url");
		}
		return locationURL;
	}

	/**
	 * 
	 * 营收MapReduce的版本号
	 * @author sid
	 * @return
	 */
	public static String getYyVersion(){
		if (yyVersion==null) {
			yyVersion = MapReducePropertiesUtil.getProperties().getProperty("ys.mapreduce.version");
		}
		return yyVersion;
	}

	/**
	 * 
	 * 待处理营收数据表名
	 * @author sid
	 * @return
	 */
	public static String getYyTb(){
		if (yyTb==null) {
			yyTb = MapReducePropertiesUtil.getProperties().getProperty("db.ys.tb");
		}
		return yyTb;
	}

	/**
	 * 
	 * 营运map方法
	 * @author sid
	 * @return
	 */
	public static String getYyMapF(){
		if (yyMapF==null) {
			yyMapF = MapReducePropertiesUtil.getProperties().getProperty("ys.map.function");
		}
		return yyMapF;
	}
	/**
	 * 
	 * 营运Reduce方法
	 *
	 * @author sid
	 * @return
	 */
	public static String getYReduceF(){
		if (yyReduceF==null) {
			yyReduceF = MapReducePropertiesUtil.getProperties().getProperty("ys.reduce.function");
		}
		return yyReduceF;
	}
	/**
	 * 营运reduce统计库名
	 *
	 * @author sid
	 * @return
	 */
	public static String getYyMapreduce(){
		if (yyMapreduce==null) {
			yyMapreduce = MapReducePropertiesUtil.getProperties().getProperty("db.ys.reduce");
		}
		return yyMapreduce;
	}
	/**
	 * 营运循环统计表名
	 *
	 * @author sid
	 * @return
	 */
	public static String getYyInterval(){
		if (yyInterval==null) {
			yyInterval = MapReducePropertiesUtil.getProperties().getProperty("db.ys.reduce.interval");
		}
		return yyInterval;
	}
	/**
	 * 营运统计开始时间
	 *
	 * @author sid
	 * @return
	 */
	public static String getYyStart(){
		if (yyStart==null) {
			yyStart = MapReducePropertiesUtil.getProperties().getProperty("ys.start");
		}
		return yyStart;
	}
	/**
	 * 待处理营收数据库前缀
	 *
	 * @author sid
	 * @return
	 */
	public static String getYyPrefix(){
		if (yyPrefix==null) {
			yyPrefix = MapReducePropertiesUtil.getProperties().getProperty("db.ys.prefix");
		}
		return yyPrefix;
	}
	/**
	 * 基础信息数据库名
	 *
	 * @author sid
	 * @return
	 */
	public static String getDbInfo(){
		if (dbInfo==null) {
			dbInfo = MapReducePropertiesUtil.getProperties().getProperty("db.info");
		}
		return dbInfo;
	}
	/**
	 * tcpuser基础信息表名
	 *
	 * @author sid
	 * @return
	 */
	public static String getTcpUser(){
		if (tcpUser==null) {
			tcpUser = MapReducePropertiesUtil.getProperties().getProperty("tcp.user.tb");
		}
		return tcpUser;
	}
	/**
	 * 营收数据循环统计间隔
	 *
	 * @author sid
	 * @return
	 */
	public static int getInterval(){
		if (interval==null) {
			interval = Integer.valueOf(MapReducePropertiesUtil.getProperties().getProperty("ys.interval"));
		}
		return interval;
	}
	/**
	 * 营收数据统计延迟时间
	 *
	 * @author sid
	 * @return
	 */
	public static int getDelay(){
		if (delay==null) {
			delay = Integer.valueOf(MapReducePropertiesUtil.getProperties().getProperty("ys.delay"));
		}
		return delay;
	}
}

