/**
 * Project Name:mapreduce
 * File Name:SumMapReduce.java
 * Package Name:mapreduce.test
 * Date:2014年12月30日上午9:26:35
 * Copyright (c) 2014, sid Jenkins All Rights Reserved.
 * 
 *
*/

package utils.db.mongodb.test;

import java.util.HashMap;

import utils.db.mongodb.main.MongodbUtil;
import utils.utils.LogUtil;

import com.mongodb.DBObject;
import com.mongodb.MapReduceOutput;

/**
 * ClassName:SumMapReduce
 * Date:     2014年12月30日 上午9:26:35 
 * @author   sid
 * @see 	 
 */
public class SumMapReduce {
	

	/**
	 * 去重：{ distinct: 't_taxi_ssys', key: 'n02' }
	 * db.addresses.distinct("zip-code");
	 * 范围：{n07:{$gt:'20141225000000',$lte:'20141225240000'}}
	 * @author sid
	 * @param args
	 */
	public static void main(String[] args) {
		String databasename="database_make_500100_2014", collectionname="t_taxi_ssys";
		String before="20141225000000";
		String end = "20141225240000";

		HashMap<String, HashMap<String, Object>> hashMap = new HashMap<String, HashMap<String, Object>>();
//		HashMap<String, Object> scsj = new HashMap<String, Object>();
//		scsj.put("$gt", before);
//		scsj.put("$lte", end);
//		hashMap.put("n05", scsj);
		HashMap<String, Object> xcsj = new HashMap<String, Object>();
		xcsj.put("$gt", before);
		xcsj.put("$lte", end);
		hashMap.put("n07", xcsj);

		//mapreduce测试
		String mapF = "function(){var category='day';emit(category, {jyje:this.n14,yylc: this.n11,kslc:this.n12,ryfjf:this.n13,dhsj:this.n14,scsj:this.n05,xcsj:this.n07});}";
//		String reduceF = "function(key, values) {var result = {jyje:0,yylc:0,kslc:0,ryfjf:0,dhsj:0,yycs:0,zksj:0}; values.forEach(function(doc) {var xcsj=String(doc.xcsj);var scsj=String(doc.scsj);if(xcsj!='undefined'&&scsj!='undefined'){var time1 = new Date(scsj.substring(0,4),scsj.substring(4,6),scsj.substring(6,8),scsj.substring(8,10),scsj.substring(10,12),scsj.substring(12,14) );var time2 = new Date(xcsj.substring(0,4),xcsj.substring(4,6),xcsj.substring(6,8),xcsj.substring(8,10),xcsj.substring(10,12),xcsj.substring(12,14) );result.zksj+=((time2.getTime()-time1.getTime())/1000/60);};result.jyje += doc.jyje; result.yylc += doc.yylc;result.kslc += doc.kslc;result.ryfjf += doc.ryfjf;result.dhsj += doc.dhsj;result.yycs += 1;}); return {timestamp:'@timestamp',jyje: result.jyje,yylc: result.yylc,kslc:result.kslc,ryfjf:result.ryfjf,dhsj:result.dhsj,yycs:result.yycs,zksc:result.zksj};}";
		String reduceF = "function(key, values) {var result = {jyje:0,yylc:0,kslc:0,ryfjf:0,dhsj:0,yycs:0,cls:0,zdje:0,};"
				+ "var carnum='';var num=0; "
				+ "values.forEach(function(doc) {"
				+ "var xcsj=String(doc.xcsj);"
				+ "var scsj=String(doc.scsj);"
				+ "result.jyje += doc.jyje; "
				+ "result.yylc += doc.yylc;"
				+ "result.kslc += doc.kslc;"
//				+ "if(carnum!=key){result.cls+=1;};"
				+ "result.yycs+=1;"
				+ "num+=1;"
				+ "if(doc.jyje>result.zdje){result.zdje=doc.jyje;};"
				+ "});"
				+ "carnum=key;"
				+ "return {jyje: result.jyje,yylc: result.yylc,kslc:result.kslc,yycs:result.yycs,cls:result.cls,zdje:result.zdje,num:num};}";

		MapReduceOutput out = MongodbUtil.getInstance().mapReduce(databasename, 
				collectionname, mapF, reduceF,"sum_test",hashMap);
		LogUtil.getInstance().getLogger(MapReduceTest.class).info("----------------------");
		int num = 0;
		for (DBObject o : out.results()) {  
			LogUtil.getInstance().getLogger(MapReduceTest.class).info(o.toString());
			num++;
		} 
		System.out.println("num:"+num);
		//重庆车辆数：7214
	}
}

