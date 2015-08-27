/**
 * Project Name:main
 * File Name:MongodbCollectionManagerUtil.java
 * Package Name:com.hdsx.taxi.driver.cq.Collection
 * Date:2014年4月9日下午12:49:55
 * Copyright (c) 2014, sid Jenkins All Rights Reserved.
 * 
 *
*/

package utils.db.mongodb.main;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.WriteResult;

import utils.db.mongodb.init.InitMongo;
import utils.db.mongodb.utils.BeanUtil;
import utils.utils.DataTypeUtil;
import utils.utils.LogUtil;


/**
 * 
 * ClassName: MongodbManagerUtil 
 * date: 2014年10月22日 上午10:22:36 
 * 
 * 条件操作符
 * $gt : >
 * $lt : <
 * $gte: >=
 * $lte: <=
 * $ne : !=、<>
 * $in : in
 * $nin: not in
 * $all: all
 * $not: 反匹配(1.3.3及以上版本)
 *
 * @author sid
 */
public class MongodbUtil {
	
	public static final String OR = "$or";
	public static final String IN = "$in";
	
	private static String hashKey = "_id";
	private static String hashValue="hashed";

	private static MongoClient mg = null;
	
	private static ConcurrentHashMap<String, DB> dbs = new ConcurrentHashMap<String, DB>();
	
	private static ConcurrentHashMap<String, DBCollection> cols = new ConcurrentHashMap<String, DBCollection>();
	
	private volatile static MongodbUtil singleton = null;
	
	private static volatile boolean isShard = true;
	
	public static MongodbUtil getInstance() {
		if (singleton == null) {
			synchronized (MongodbUtil.class) {
				if (singleton == null) {
					singleton = new MongodbUtil();
				}
			}
			singleton = new MongodbUtil();
		}
		return singleton;
	}

	private MongodbUtil() {
		if (LogUtil.getInstance().getLogger(MongodbUtil.class).isDebugEnabled()) {
			LogUtil.getInstance().getLogger(MongodbUtil.class).debug("MongodbCollectionManagerUtil() - start "); //$NON-NLS-1$
		}
		
		if (mg==null) {
			try {
				List<ServerAddress> addresses = new ArrayList<ServerAddress>();
				if (DataTypeUtil.isNotEmpty(InitMongo.SERVER_HOSTS)) {
					isShard = true;
					LogUtil.getInstance().getLogger(this.getClass()).info("-----启动集群分片数据库：{}",InitMongo.SERVER_HOSTS);
					String[] list = InitMongo.SERVER_HOSTS.split(",");
					for (String ip : list) {
						ServerAddress address = new ServerAddress(ip, InitMongo.SERVER_PORT);
						addresses.add(address);
					}
					mg = new MongoClient(addresses);
				}else{
					isShard = false;
					LogUtil.getInstance().getLogger(this.getClass()).info("启动单机数据库：{}",InitMongo.SERVER_HOST);
					mg = new MongoClient(InitMongo.SERVER_HOST, InitMongo.SERVER_PORT);
				}
				dbs.put(InitMongo.DATABASE_INFO, getDB(InitMongo.DATABASE_INFO));
				dbs.put(InitMongo.DATABASE_MAKE, getDB(InitMongo.DATABASE_MAKE));
				dbs.put(InitMongo.DATABASE_TRACK, getDB(InitMongo.DATABASE_TRACK));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}

//		MongoClientOptions.Builder build = new MongoClientOptions.Builder();
//		/**
//		 * 是否连接自动重试
//		 */
//		//build.autoConnectRetry(true);
//
//		/**
//		 * 连接数
//		 */
//		build.connectionsPerHost(Integer.valueOf(KeegooConfig.mongodbConnectionsCount));
//
//		/**
//		 * 连接超时时间
//		 */
//		build.connectTimeout(30000);
//		/**
//		 * 最大等待时间
//		 */
//		build.maxWaitTime(120000);
//		/**
//		 * 保持连接
//		 */
//		build.socketKeepAlive(true);
//		/**
//		 * 0，不限时间
//		 */
//		build.socketTimeout(0);
//		/**
//		 * 最大重试时间，单位秒
//		 */
//		//build.maxAutoConnectRetryTime(1);
//
//		build.threadsAllowedToBlockForConnectionMultiplier(50);
//		MongoClientOptions mongoClientOptions = build.build();
//
//		instance = new MongoClient(Arrays.asList(mongos1, mongos2, mongos3), mongoClientOptions);
		if (LogUtil.getInstance().getLogger(MongodbUtil.class).isDebugEnabled()) {
			LogUtil.getInstance().getLogger(MongodbUtil.class).debug("MongodbCollectionManagerUtil() - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 创建分片数据库
	 * createShardDB:
	 *
	 * @author sid
	 * @param databasename
	 */
	private void createShardDB(String databasename) {
		LogUtil.getInstance().getLogger(getClass()).info("创建新的数据库{},并分片",databasename);
		DB admin = mg.getDB("admin");
		DBObject obj = new BasicDBObject();
		obj.put("enablesharding", databasename);
		CommandResult command = admin.command(obj);
		LogUtil.getInstance().getLogger(getClass()).info("分片结果{}",command);
	}

	/**
	 * 创建表，并进行分片
	 * createShardCollection:
	 *
	 * @author sid
	 * @param databasename
	 * @param collectionname
	 */
	private void createShardCollection(String databasename,
			String collectionname) {
		LogUtil.getInstance().getLogger(getClass()).info("创建新的表{}.{},并分片",databasename,collectionname);
		DB admin = mg.getDB("admin");
		DBObject shard = new BasicDBObject();
		DBObject key = new BasicDBObject();
		key.put(hashKey, hashValue);
		shard.put("shardcollection", databasename+"."+collectionname);
		shard.put("key", key);
		CommandResult command = admin.command(shard);
		LogUtil.getInstance().getLogger(getClass()).info("分片结果{}",command);
	}

	/**
	 * 
	 * getDBs:(获取数据库列表). 
	 *
	 * @author sid
	 * @return
	 */
	public List<String> getDBs(){
		List<String> databaseNames = mg.getDatabaseNames();
		return databaseNames;
	}

	/**
	 * 
	 * getDB:(获取数据库). 
	 *
	 * @author sid
	 * @param databasename
	 * @return
	 */
	public DB getDB(String databasename){
		DB db = dbs.get(databasename);
		if (db==null) {
			if ((!mg.getDatabaseNames().contains(databasename))&&isShard) {
				createShardDB(databasename);
			}
			db = mg.getDB(databasename);
			dbs.put(databasename, db);
		}
		return db;
	}

	/**
	 * 
	 * dropDB:(删除数据库). 
	 *
	 * @author sid
	 * @param databasename
	 * @return
	 */
	public void dropDB(String databasename){
		DB db = dbs.get(databasename);
		if (db==null) {
			db = mg.getDB(databasename);
			dbs.put(databasename, db);
		}
		dbs.remove(databasename);
		db.dropDatabase();
	}
	
	/**
	 * 判断数据库是否存在
	 *
	 * @author sid
	 * @param databasename
	 * @return
	 */
	public boolean isExistDB(String databasename){
		List<String> databaseNames = mg.getDatabaseNames();
		if (databaseNames.contains(databasename)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * getCollection:(获取表集合). 
	 *
	 * @author sid
	 * @param databasename
	 * @param collectionname
	 * @return
	 */
	public DBCollection getCollection(String databasename,String collectionname){
		DBCollection dbCollection = cols.get(databasename+collectionname);
		if (dbCollection==null) {
			DB db = getDB(databasename);
			if ((!db.getCollectionNames().contains(collectionname))&&isShard) {
				createShardCollection(databasename, collectionname);
			}
			dbCollection = db.getCollection(collectionname);
			cols.put(databasename+collectionname, dbCollection);
		}
		return dbCollection;
	}

	/**
	 * 
	 * dropCollection:(删除表集合). 
	 *
	 * @author sid
	 * @param databasename
	 * @param collectionname
	 * @return
	 */
	public void dropCollection(String databasename,String collectionname){
		DBCollection dbCollection = cols.get(databasename+collectionname);
		if (dbCollection==null) {
			DB db = getDB(databasename);
			dbCollection = db.getCollection(collectionname);
			cols.put(databasename+collectionname, dbCollection);
		}
		dbCollection.drop();
	}
	/**
	 * 
	 * insert:(在指定表集合中存入的对象). 
	 *
	 * @author sid
	 * @param databasename
	 * @param collectionname
	 * @param bean
	 * @return
	 */
    public boolean insert(String databasename,String collectionname, Object bean) {  
    	boolean result = false;
    	WriteResult writeResult = null;
        DBCollection col = this.getCollection(databasename, collectionname);
        DBObject obj = BeanUtil.bean2DBObj(bean);
        writeResult = col.insert(obj);
        result = getWriteResult(result, writeResult);
        return result;
    }  
	/**
	 * 
	 * insertList:(在指定表集合中存入的对象). 
	 *
	 * @author sid
	 * @param databasename
	 * @param collectionname
	 * @param beans
	 * @return
	 */
    public boolean insertList(String databasename,String collectionname, List<?> beans) {  
    	boolean result = false;
    	WriteResult writeResult = null;
        DBCollection col = this.getCollection(databasename, collectionname);
        List<DBObject> objs = BeanUtil.beans2DBObjs(beans);
        writeResult = col.insert(objs);
        result = getWriteResult(result, writeResult);
        return result;
    }  
	/**
	 * 
	 * insert:(在指定表集合中存入的对象).
	 *
	 * @author sid
	 * @param databasename
	 * @param collectionname
	 * @param map
	 * @return
	 */
	public boolean insertMapList(String databasename, String collectionname,
			List<HashMap<String, Object>> list) {
		boolean result = false;
		WriteResult writeResult = null;
		DBCollection col = this.getCollection(databasename, collectionname);
		List<DBObject> listDBObject = new ArrayList<DBObject>();
		DBObject obj = null;
		for (HashMap<String, ?> map : list) {
			Set<String> keys = map.keySet();
			obj = new BasicDBObject();
			for (String key : keys) {
				obj.put(key, map.get(key));
			}
			listDBObject.add(obj);
		}
		writeResult = col.insert(listDBObject);
		result = getWriteResult(result, writeResult);
		return result;
	}

	/**
	 * 
	 * insert:(在指定表集合中存入的对象).
	 * save or update 
	 *
	 * @author sid
	 * @param databasename
	 * @param collectionname
	 * @param bean
	 * @return
	 */
    public boolean insertMapReduceResult(String databasename,String collectionname, DBObject obj) {  
    	boolean result = false;
    	WriteResult writeResult = null;
        DBCollection col = this.getCollection(databasename, collectionname);
        writeResult = col.save(obj);
        result = getWriteResult(result, writeResult);
        return result;
    }
	/**
	 * 
	 * insertList:(在指定表集合中存入的对象). 
	 *
	 * @author sid
	 * @param databasename
	 * @param collectionname
	 * @param beans
	 * @return
	 */
    public boolean insertMapReduceResults(String databasename,String collectionname, List<DBObject> objs) {  
    	boolean result = false;
    	WriteResult writeResult = null;
        DBCollection col = this.getCollection(databasename, collectionname);
        writeResult = col.insert(objs);
        result = getWriteResult(result, writeResult);
        return result;
    }  
	/**
	 * 
	 * insert:(在指定表集合中存入的对象). 
	 *
	 * @author sid
	 * @param databasename
	 * @param collectionname
	 * @param map
	 * @return
	 */
    public boolean insert(String databasename,String collectionname, HashMap<String, ?> map) {  
    	boolean result = false;
    	WriteResult writeResult = null;
        DBCollection col = this.getCollection(databasename, collectionname);
        Set<String> keys = map.keySet();
        DBObject obj = new BasicDBObject();
        for (String key : keys) {
        	obj.put(key, map.get(key));
		}
        writeResult = col.insert(obj);
        result = getWriteResult(result, writeResult);
        return result;
    }  
	/**
	 * 
	 * update:(在指定表集合中存入的对象). 
	 *
	 * @author sid
	 * @param databasename
	 * @param collectionname
	 * @param map
	 * @param key
	 * @return
	 * 如果不存在直接插入后返回true
	 * 如果存在则更新返回false
	 */
    public boolean update(String databasename,String collectionname,HashMap<String, ?> map, HashMap<String, ?> key) {    
    	boolean result = false;
    	WriteResult writeResult = null;
        DBCollection col = this.getCollection(databasename, collectionname);
		//组合实体
        Set<String> keySet = map.keySet();
        DBObject obj = new BasicDBObject();
        for (String k : keySet) {
        	obj.put(k, map.get(k));
		}
        
        BasicDBObject basic = makeCondition(key);
        int size = col.find(basic).count();
        if (size==0) {
        	writeResult = col.insert(obj);
            result = getWriteResult(result, writeResult);
		}else{
			DBCursor find = col.find(basic);
			while (find.hasNext()) {
				DBObject findobj = find.next();
				//获取分片的片键
	        	HashMap<String, ObjectId> one = new HashMap<String, ObjectId>();
	        	one.put(hashKey, new ObjectId(findobj.get(hashKey).toString()));
	            BasicDBObject basicOne = makeCondition(one);
				writeResult = col.update(basicOne, obj);
			}
		}
        return result;
    }  
	/**
	 * 
	 * insertMaps:(在指定表集合中存入的对象). 
	 *
	 * @author sid
	 * @param databasename
	 * @param collectionname
	 * @param maps
	 * @return
	 */
    public boolean insertMaps(String databasename,String collectionname, List<HashMap<String, ?>> maps) {  
    	boolean result = false;
    	WriteResult writeResult = null;
        DBCollection col = this.getCollection(databasename, collectionname);
        List<DBObject> objs = new ArrayList<DBObject>();
        for (HashMap<String, ?> map : maps) {
            Set<String> keys = map.keySet();
            DBObject obj = new BasicDBObject();
            for (String key : keys) {
            	obj.put(key, map.get(key));
    		}
            objs.add(obj);
		}
        writeResult = col.insert(objs);
        result = getWriteResult(result, writeResult);
        return result;
    }
	/**
	 * 
	 * update:(在指定表集合中存入的对象). 
	 *
	 * @author sid
	 * @param databasename
	 * @param collectionname
	 * @param map
	 * @param bean
	 * @return
	 */
    public boolean update(String databasename,String collectionname,HashMap<String, Object> map, Object bean) {    
    	boolean result = false;
    	WriteResult writeResult = null;
        DBCollection col = this.getCollection(databasename, collectionname);
		
        DBObject obj = BeanUtil.bean2DBObj(bean);
        BasicDBObject basic = makeCondition(map);
        int size = col.find(basic).count();
        if (size==0) {
        	writeResult = col.insert(obj);
		}else{
			DBCursor find = col.find(basic);
			while (find.hasNext()) {
				DBObject next = find.next();
				obj.put("_id", next.get("_id"));
				writeResult = col.update(next, obj);
			}
		}
        result = getWriteResult(result, writeResult);
        return result;
    }  
    
    /**
     * 
     * remove:(从指定集合对象中清除对象). 
     *
     * @author sid
     * @param databasename
     * @param collectionname
     * @param map
     * @return
     */
    public boolean remove(String databasename,String collectionname, HashMap<String, Object> map) { 
    	boolean result = false;
    	WriteResult writeResult = null;
        DBCollection col = this.getCollection(databasename, collectionname);
        BasicDBObject basic = makeCondition(map);
        writeResult = col.remove(basic);
        if (null != writeResult) {
            result = getWriteResult(result, writeResult);
            if (!result) {
            	LogUtil.getInstance().getLogger(MongodbUtil.class)
            			.info(collectionname+"表中id为"+map.toString()+"的数据不存在");
            }
        }
        return result;
    }  
    
    /**
     * 
     * getList:(根据条件过滤集合). 
     *
     * @author sid
     * @param databasename
     * @param collectionname
     * @param clazz
     * @param basic
     * @return
     */
	private <T> List<T> getListBySort(String databasename, String collectionname,
			Class<T> clazz, BasicDBObject basic,HashMap<String, Object> sort,int limit) {
		List<T> list = new ArrayList<T>();
		DBObject sortObject = new BasicDBObject();
		Set<String> keySet = sort.keySet();
		for (String key : keySet) {
			sortObject.put(key, sort.get(key));
		}
        try {
			DBCollection col = this.getCollection(databasename, collectionname);
			DBCursor find = col.find(basic).sort(sortObject).limit(limit);
			while (find.hasNext()) {
				DBObject dbObject = find.next();
				T b = clazz.newInstance();
				b = BeanUtil.dbObj2Bean(dbObject, b);
				list.add(b);
			}
		} catch (Exception e) {
			LogUtil.getInstance().getLogger(MongodbUtil.class)
			.error("根据指定条件获取"+collectionname+"中的部分数据异常", e);
			e.printStackTrace();
		}
        return list;
	}
  
    /**
     * 
     * getBySort:(根据过滤条件从指定缓存对象中获取对象). 
     *
     * @author sid
     * @param databasename
     * @param collectionname
     * @param map
     * @param clazz
     * @return
     */
    public <T> List<T> getBySort(String databasename,String collectionname, 
    		HashMap<String, Object> map, Class<T> clazz,HashMap<String, Object> sort,int limit) {
        BasicDBObject basic = makeCondition(map);
		return getListBySort(databasename, collectionname, clazz, basic, sort, limit);
    }
  
    /**
     * 
     * get:(根据过滤条件从指定缓存对象中获取对象). 
     *
     * @author sid
     * @param databasename
     * @param collectionname
     * @param map
     * @param bean
     * @return
     */
    public <T> List<T> get(String databasename,String collectionname, HashMap<String, Object> map, Class<T> clazz) {
		BasicDBObject basic = makeCondition(map);
		return getList(databasename, collectionname, clazz, basic);
    }
  
    /**
     * 
     * get:(根据过滤条件从指定缓存对象中获取对象). 
     *
     * @author sid
     * @param databasename
     * @param collectionname
     * @param map
     * @param bean
     * @return
     */
    public List<DBObject> get(String databasename,String collectionname, HashMap<String, Object> map) {
		BasicDBObject basic = makeCondition(map);
		return getList(databasename, collectionname, basic);
    }
    /**
     * 
     * getList:(根据条件过滤集合). 
     *
     * @author sid
     * @param databasename
     * @param collectionname
     * @param clazz
     * @param basic
     * @return
     */
	private List<DBObject> getList(String databasename, String collectionname, BasicDBObject basic) {
		List<DBObject> list = new ArrayList<DBObject>();
        try {
			DBCollection col = this.getCollection(databasename, collectionname);
			DBCursor find = col.find(basic);
			while (find.hasNext()) {
				DBObject dbObject = find.next();
				list.add(dbObject);
			}
		} catch (Exception e) {
			LogUtil.getInstance().getLogger(MongodbUtil.class)
			.error("根据指定条件获取"+collectionname+"中的部分数据异常", e);
			e.printStackTrace();
		}
        return list;
	}

    
    /**
     * 根据过滤条件从指定缓存对象中获取对象个数
     * getCount:
     *
     * @author sid
     * @param databasename
     * @param collectionname
     * @param map
     * @return
     */
    public int getCount(String databasename,String collectionname, HashMap<String, Object> map) {
        BasicDBObject basic = makeCondition(map);
        try {
			DBCollection col = this.getCollection(databasename, collectionname);
			int find = col.find(basic).count();
			return find;
		} catch (Exception e) {
			LogUtil.getInstance().getLogger(MongodbUtil.class)
			.error("根据指定条件获取"+collectionname+"中的部分数据异常", e);
			e.printStackTrace();
		}
        return 0;
    }
  
    /**
     * 从指定缓存对象中获取对象个数
     * getCount:
     *
     * @author sid
     * @param databasename
     * @param collectionname
     * @return
     */
    public int getCount(String databasename,String collectionname) {
        try {
			DBCollection col = this.getCollection(databasename, collectionname);
			int find = col.find().count();
			return find;
		} catch (Exception e) {
			LogUtil.getInstance().getLogger(MongodbUtil.class)
			.error("根据指定条件获取"+collectionname+"中的部分数据异常", e);
			e.printStackTrace();
		}
        return 0;
    }

    /**
     * 
     * getByCondition:(根据过滤条件从指定缓存对象中获取对象). 
     *
     * @author sid
     * @param databasename
     * @param collectionname
     * @param map
     * @param clazz
     * @return
     */
    public <T> List<T> getByCondition(String databasename,String collectionname, 
    		Object map, Class<T> clazz) {
		BasicDBObject basic = makeCondition(map);
		return getList(databasename, collectionname, clazz, basic);
    }
    /**
     * 根据过滤条件从指定缓存对象中获取对象
     * getByMultiCondition:
     *
     * @author sid
     * @param databasename
     * @param collectionname
     * @param condition :$or、$in
     * @param map
     * @param clazz
     * @return
     */
    public <T> List<T> getByMultiCondition(String databasename,String collectionname, MongoCondition condition,
    		HashMap<String, HashMap<String, Object>> map, Class<T> clazz) {
		BasicDBObject basic = multiCondition(condition.getVal(),map);
		return getList(databasename, collectionname, clazz, basic);
    }
    /**
     * 
     * getList:(根据条件过滤集合). 
     *
     * @author sid
     * @param databasename
     * @param collectionname
     * @param clazz
     * @param basic
     * @return
     */
	private <T> List<T> getList(String databasename, String collectionname,
			Class<T> clazz, BasicDBObject basic) {
		List<T> list = new ArrayList<T>();
        try {
			DBCollection col = this.getCollection(databasename, collectionname);
			DBCursor find = col.find(basic);
			while (find.hasNext()) {
				DBObject dbObject = find.next();
				T b = clazz.newInstance();
				b = BeanUtil.dbObj2Bean(dbObject, b);
				list.add(b);
			}
		} catch (Exception e) {
			LogUtil.getInstance().getLogger(MongodbUtil.class)
			.error("根据指定条件获取"+collectionname+"中的部分数据异常", e);
			e.printStackTrace();
		}
        return list;
	}
    
    /**
     * 
     * getAll:(获取指定集合中的所有数据). 
     *
     * @author sid
     * @param databasename
     * @param collectionname
     * @param clazz
     * @return
     */
	public <T> List<T> getAll(String databasename,String collectionname, Class<T> clazz) {
        DBCollection col = this.getCollection(databasename, collectionname);
		List<T> list = new ArrayList<T>();
        try {
			DBCursor find = col.find();
			while (find.hasNext()) {
				DBObject dbObject = find.next();
				T b = clazz.newInstance();
				b = BeanUtil.dbObj2Bean(dbObject, b);
				list.add(b);
			}
		} catch (Exception e) {
			LogUtil.getInstance().getLogger(MongodbUtil.class)
					.error("获取"+collectionname+"中所有数据异常", e);
			e.printStackTrace();
		}
        return list;
    }
    
    /**
     * 获取指定集合中的所有数据
     * getAllSort:
     *
     * @author sid
     * @param databasename
     * @param collectionname
     * @param clazz
     * @param sort
     * @param limit
     * @return
     */
	public <T> List<T> getAllSort(String databasename,String collectionname, Class<T> clazz,
			HashMap<String, Object> sort,int limit) {
        DBCollection col = this.getCollection(databasename, collectionname);
		List<T> list = new ArrayList<T>();
		DBObject sortObject = new BasicDBObject();
		Set<String> keySet = sort.keySet();
		for (String key : keySet) {
			sortObject.put(key, sort.get(key));
		}
        try {
			DBCursor find = col.find().sort(sortObject).limit(limit);
			while (find.hasNext()) {
				DBObject dbObject = find.next();
				T b = clazz.newInstance();
				b = BeanUtil.dbObj2Bean(dbObject, b);
				list.add(b);
			}
		} catch (Exception e) {
			LogUtil.getInstance().getLogger(MongodbUtil.class)
					.error("获取"+collectionname+"中所有数据异常", e);
			e.printStackTrace();
		}
        return list;
    }
    
    /**
     * 
     * getAll:(获取指定集合中的所有数据). 
     *
     * @author sid
     * @param databasename
     * @param collectionname
     * @return
     */
	public List<DBObject> getAll(String databasename,String collectionname) {
        DBCollection col = this.getCollection(databasename, collectionname);
		List<DBObject> list = new ArrayList<DBObject>();
        try {
			DBCursor find = col.find();
			while (find.hasNext()) {
				DBObject dbObject = find.next();
				list.add(dbObject);
			}
		} catch (Exception e) {
			LogUtil.getInstance().getLogger(MongodbUtil.class)
					.error("获取"+collectionname+"中所有数据异常", e);
			e.printStackTrace();
		}
        return list;
    }

	/**
	 * 
	 * mapReduce:(对指定表集合的所有数据进行mapreduce操作). 
	 *
	 * @author sid
	 * @param databasename
	 * @param collectionname
	 * @param mapFunction
	 * @param reduceFunction
	 * @param resultname
	 * @return
	 */
	public MapReduceOutput mapReduce(String databasename,String collectionname,
			String mapFunction,String reduceFunction,String resultname) {
		MapReduceOutput out = this.mapReduce(databasename, collectionname, 
				mapFunction, reduceFunction, resultname,null);
        return out;
	}
	/**
	 * 
	 * mapReduce:(对指定表集合的部分数据进行mapreduce操作).
	 * map传入过滤条件
	 *
	 * @author sid
	 * @param databasename
	 * @param collectionname
	 * @param mapFunction
	 * @param reduceFunction
	 * @param resultname
	 * @param map
	 * @return
	 */
	public MapReduceOutput mapReduce(String databasename,String collectionname,String mapFunction,
			String reduceFunction,String resultname,HashMap<String, HashMap<String, Object>> map) {
        DBCollection col = this.getCollection(databasename, collectionname);
        BasicDBObject basic = makeCondition(map);
        MapReduceCommand command = new MapReduceCommand(col, mapFunction, reduceFunction,
        		resultname, MapReduceCommand.OutputType.INLINE, basic);
        MapReduceOutput out = col.mapReduce(command);  
        return out;
	}

    /**
     * 
     * getWriteResult:(获取插入结果).
     *
     * @author sid
     * @param result
     * @param writeResult
     * @return
     */
	@SuppressWarnings("deprecation")
	private boolean getWriteResult(boolean result, WriteResult writeResult) {
		if (null != writeResult) {
            if (writeResult.getField("ok")!=null&&(int)writeResult.getField("ok")>0) {
                result = true;
            }
        }
		return result;
	}  

	/**
	 * 组装查询条件
	 * getByCondition:
	 *
	 * @author sid
	 * @param map
	 * @return
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Object makeCondition(String key,Object obj) {
		BasicDBObject basic = new BasicDBObject();
    	
        if (obj!=null) {
        	if (obj instanceof HashMap<?, ?>) {
        		HashMap<String, Object> map = (HashMap)obj;
				Set<String> keys = map.keySet();
                basic = new BasicDBObject();
                for (String k : keys) {
                	Object basicObject = makeCondition(k,map.get(k));
                	basic.append(k, basicObject);
        		}
			}else{
				return obj;
			}
		}
		return basic;
	}
	/**
	 * 组装查询条件
	 * getByCondition:
	 *
	 * @author sid
	 * @param map
	 * @return
	 */
	public BasicDBObject makeCondition(Object obj) {
		return (BasicDBObject)makeCondition(null, obj);
	}
	
	/**
	 * 组合过滤条件
	 * multiCondition:
	 *
	 * @author sid
	 * @param condition：$or,$in
	 * @param obj
	 * @return
	 */
	public BasicDBObject multiCondition(String condition,HashMap<String, HashMap<String, Object>> map){
		BasicDBObject result = new BasicDBObject();
		String[] keys = (String[]) map.keySet().toArray(new String[0]);
		BasicDBList basic = new BasicDBList();
        for (int i=0;i<keys.length;i++) {
        	Object basicObject = makeCondition(keys[i],map.get(keys[i]));
        	basic.add(new BasicDBObject(keys[i], basicObject));
		}
        result.put(condition, basic);
		return result;
	}
}

