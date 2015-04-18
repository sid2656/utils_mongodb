/**
 * Project Name:mapreduce
 * File Name:MongoUtilTest.java
 * Package Name:mapreduce.test
 * Date:2014年12月1日下午7:50:02
 * Copyright (c) 2014, sid Jenkins All Rights Reserved.
 * 
 *
*/

package mapreduce.test;

import java.util.HashMap;
import java.util.List;

import com.hdsx.taxi.mongo.MongoCondition;
import com.hdsx.taxi.mongo.MongodbUtil;
import com.mongodb.DBObject;

/**
 * ClassName:MongoUtilTest
 * Function: TODO ADD FUNCTION. 
 * Reason:	 TODO ADD REASON. 
 * Date:     2014年12月1日 下午7:50:02 
 * @author   sid
 * @see 	 
 */
public class MongoUtilTest {

	public static void main(String[] args) {
//		getByMultiCondition();
//		getBySortCondition();
		getBySort();
	}

	private static void getBySortCondition() {
		String databasename = "database_info";
		String collectionname = "t_taxi_locationcount";
		HashMap<String, Object> xcsj = new HashMap<String, Object>();
		xcsj.put("nowDate", -1);
		
		List<YyxlCl> all = MongodbUtil.getInstance().getAllSort(databasename, collectionname, 
				YyxlCl.class, xcsj, 5);
		for (YyxlCl yyxlCl : all) {
			System.out.println(yyxlCl.getEmptyCarCount());
		}

		System.out.println(all.size());
	}
	
	private static void getBySort(){
		String databasename = "database_info";
		String collectionname = "t_taxi_locationcount";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("xzqhdm", null);
		HashMap<String, Object> xcsj = new HashMap<String, Object>();
		xcsj.put("Date", -1);
		List<YyxlCl> allSort = MongodbUtil.getInstance().getBySort(databasename, 
				collectionname, map, YyxlCl.class, xcsj, 1);
		for (YyxlCl yyxlCl : allSort) {
			System.out.println(yyxlCl.getEmptyCarCount());
		}
	}

	private static void getByMultiCondition() {
		String databasename = "database_make_500100_2014";
		String collectionname = "t_taxi_ssys";
		HashMap<String, HashMap<String, Object>> hashMap = new HashMap<String, HashMap<String, Object>>();
		HashMap<String, Object> scsj = new HashMap<String, Object>();
		scsj.put("$gt", "20141130140000");
		scsj.put("$lte", "20141201140000");
		hashMap.put("n05", scsj);
		HashMap<String, Object> xcsj = new HashMap<String, Object>();
		xcsj.put("$gt", "20141201120000");
		xcsj.put("$lte", "20141201140000");
		hashMap.put("n07", xcsj);
		
		List<MapReduce> all = MongodbUtil.getInstance().getByMultiCondition(databasename, 
				collectionname,MongoCondition.OR,hashMap, MapReduce.class);

		System.out.println(all.size());
	}
}
