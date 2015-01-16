/**
 * Project Name:mongo
 * File Name:MapReduceTest.java
 * Package Name:com.hdsx.taxi.mongo
 * Date:2014年10月23日下午12:28:49
 * Copyright (c) 2014, sid Jenkins All Rights Reserved.
 * 
 *
*/

package utils.db.mongodb.test;

import java.util.HashMap;
import java.util.List;

import utils.db.mongodb.main.MongodbUtil;
import utils.utils.LogUtil;

import com.mongodb.DBObject;
import com.mongodb.MapReduceOutput;

/**
 * ClassName:MapReduceTest
 * Date:     2014年10月23日 下午12:28:49 
 * @author   sid
 * @see 	 
 */
public class MapReduceTest {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		String databasename="czc_test", collectionname="bean";
		Class<?> clazz = BeanTest.class;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("sex", "女");
		HashMap<String, HashMap<String, Object>> hashMap = new HashMap<String, HashMap<String, Object>>();
		HashMap<String, Object> condition = new HashMap<String, Object>();
		condition.put("$gte", 14);
		hashMap.put("age", condition);

		//存储测试
//		BeanTest bean1 = new BeanTest("王五", "女",20,new Date());
//		BeanTest bean2 = new BeanTest("赵六", "男",20,new Date());
//
//		MongodbUtil.getInstance().insert("czc_test", "bean", bean1);
//		MongodbUtil.getInstance().insert("czc_test", "bean", bean2);
//		
//		List<BeanTest> all = MongodbUtil.getInstance().getAll(databasename, collectionname, clazz);
//		for (BeanTest beanTest : all) {
//			LogUtil.getInstance().getLogger(MapReduceTest.class).info(beanTest.getName()+beanTest.getAge());
//		}
		


//		LogUtil.getInstance().getLogger(MapReduceTest.class).info("----------------------");
//		List<BeanTest> list = MongodbUtil.getInstance().get(databasename, collectionname, map, clazz);
//		for (BeanTest beanTest : list) {
//			LogUtil.getInstance().getLogger(MapReduceTest.class).info(beanTest.getName()+beanTest.getAge());
//		}
        
		//mapreduce测试
		String mapF = "function() { "+   
				"var category; " +    
				"if ( this.age >= 13 ) "+    
				"category = 'Big '; " +  
				"else " +  
				"category = 'Small '; "+    
				"emit(category, {name: this.name});}";
//		String reduceF = "function(key, values) { " +  
//				"var sum = 0; " +  
//				"values.forEach(function(doc) { " +  
//				"sum += 1; "+  
//				"}); " +  
//				"return {person: sum};} ";
		String reduceF = "function(key, values) { " +  
				"var Big = $BIG; " +  
				"var Small = $SMALL; " +  
				"var result = Big; "
				+ "if(key=='Small '){result = Small;}" +  
				"var sum = 0; " +  
				"values.forEach(function(doc) { " +  
				"result.person += 1; "+   
				"result.current += 1; "+  
				"sum += 1; "+  
				"}); " +  
				"return {person: result.person,current: result.current,sum:sum};} ";
		
		List<DBObject> all = MongodbUtil.getInstance().getAll(databasename, "myresults");
		String initStr = "";
		for (DBObject dbObject : all) {
			LogUtil.getInstance().getLogger(MapReduceTest.class).info(dbObject.toString());
			if (dbObject.get("_id").equals("Big ")) {
				initStr = dbObject.get("value").toString();
				reduceF = reduceF.replace("$BIG", initStr);
			}else if (dbObject.get("_id").equals("Small ")) {
				initStr = dbObject.get("value").toString();
				reduceF = reduceF.replace("$SMALL", initStr);
			}
		}

		reduceF = reduceF.replace("$BIG", "{person: 0,current: 0,sum: 0}");
		reduceF = reduceF.replace("$SMALL", "{person: 0,current: 0,sum: 0}");
		
		MapReduceOutput out1 = MongodbUtil.getInstance().mapReduce(databasename, collectionname, mapF, reduceF,"myresults");

		LogUtil.getInstance().getLogger(MapReduceTest.class).info("----------------------");
		for (DBObject o : out1.results()) {  
			LogUtil.getInstance().getLogger(MapReduceTest.class).info(o.toString());
		} 
		
		
//		LogUtil.getInstance().getLogger(MapReduceTest.class).info("----------------------");
//		MapReduceOutput out2 = MongodbUtil.getInstance().mapReduce(databasename, collectionname, mapF, reduceF,"myresults2",hashMap);
//		for (DBObject o : out2.results()) {  
//			LogUtil.getInstance().getLogger(MapReduceTest.class).info(o.toString());
//		} 
        

//		List<DBObject> all = MongodbUtil.getInstance().getAll(databasename, "myresults");
//		for (DBObject beanTest : all) {
//			LogUtil.getInstance().getLogger(MapReduceTest.class).info(beanTest.toString());
//		}
	}
}

