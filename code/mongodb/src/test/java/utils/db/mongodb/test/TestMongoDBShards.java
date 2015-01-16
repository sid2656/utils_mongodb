package utils.db.mongodb.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.db.mongodb.main.MongodbUtil;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;


/**
 * 2014年10月31日 下午2:53:31
 * 
 * @author sid
 * @see
 */
public class TestMongoDBShards {
	public static void main(String[] args) {
//		shardTest();
		for (int i=0;i<20;i++) {
			mongoUtilShardSaveTest(i);
		}
		
//		MongodbUtil.getInstance().dropDB("testshard");
		
		
//		mongoUtilTest();
//		insertTest();
	}
	@SuppressWarnings("rawtypes")
	private static void insertTest() {
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("n01", "00001937");
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("n02", "00001937");
		List list = new ArrayList<HashMap>();
		list.add(map1);
		list.add(map2);
		long start = System.currentTimeMillis();
		boolean insert = MongodbUtil.getInstance().insertMaps("mySemaphoreTest", "mounthOperation", list);
		long end = System.currentTimeMillis();
		long time = end-start;
		System.out.println("当前n01: 00001937查询mySemaphoreTest_mounthOperation操作用时："+time+"毫秒，结果："+insert);
	}

	@SuppressWarnings("rawtypes")
	private static void mongoUtilTest() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("n01", "00001937");
		long start = System.currentTimeMillis();
		List<HashMap> all = MongodbUtil.getInstance().get("mySemaphoreTest", "mounthOperation", map, HashMap.class);
		long end = System.currentTimeMillis();
		long time = end-start;
		System.out.println("当前n01: 00001937查询mySemaphoreTest_mounthOperation操作用时："+time+"毫秒，"+(time/1000)+"秒，结果集："+all.size());
	}

	private static void mongoUtilShardTest(int i) {
		DBCollection coll = MongodbUtil.getInstance().getCollection("testshard", "t_count");
		BasicDBObject object = new BasicDBObject();
		object.append("id", i);
		DBObject dbObject = coll.findOne(object);
		System.out.println(dbObject);
	}

	private static void mongoUtilShardSaveTest(int i) {
		DBCollection coll = MongodbUtil.getInstance().getCollection("testshard1", "t_count");
		BasicDBObject object = new BasicDBObject();
		object.append("id", i);
		coll.save(object);
		System.out.println(object);
	}

	private static void shardTest() {
		try {
			List<ServerAddress> addresses = new ArrayList<ServerAddress>();
			ServerAddress address1 = new ServerAddress("192.168.0.170", 27000);
			ServerAddress address2 = new ServerAddress("192.168.0.171", 27000);
			ServerAddress address3 = new ServerAddress("192.168.0.172", 27000);
			addresses.add(address1);
			addresses.add(address2);
			addresses.add(address3);
			MongoClient client = new MongoClient(addresses);
			DB db = client.getDB("testshard");
			DBCollection coll = db.getCollection("t_count");
			BasicDBObject object = new BasicDBObject();
			object.append("id", 88888);
			DBObject dbObject = coll.findOne(object);
			System.out.println(dbObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
