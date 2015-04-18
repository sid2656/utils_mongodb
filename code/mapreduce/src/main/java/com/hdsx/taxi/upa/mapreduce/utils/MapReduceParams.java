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
 * Function: TODO ADD FUNCTION. 
 * Reason:	 TODO ADD REASON. 
 * Date:     2014年12月1日 上午10:20:22 
 * @author   sid
 * @see 	 
 */
public class MapReduceParams {

	private static String params = null;//营收数据循环统计间隔
	private static String sign = null;//参数标识头

	public static String getSign(){
		if (sign==null) {
			sign = MapReducePropertiesUtil.getProperties().getProperty("ys.params.sign");
		}
		return sign;
	}
	
	public static String getParams(){
		if (params==null) {
			params = MapReducePropertiesUtil.getProperties().getProperty("ys.reduce.params");
		}
		return params;
	}
	
	public static String replaceParams(String data){
		String[] strs = params.split(",");
		for (String param : strs) {
			data.replace(sign+param, MapReducePropertiesUtil.getProperties().getProperty("ys.params."+param));
		}
		return data;
	}
}

