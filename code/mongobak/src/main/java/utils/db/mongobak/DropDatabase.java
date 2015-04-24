/**
 * Project Name:compress
 * File Name:DropDatabase.java
 * Package Name:utils.compress.mongo
 * Date:2015年4月24日上午10:13:04
 * Copyright (c) 2015, sid Jenkins All Rights Reserved.
 * 
 *
*/

package utils.db.mongobak;

import java.util.List;

import org.slf4j.Logger;

import utils.db.mongodb.main.MongodbUtil;
import utils.utils.DataTypeUtil;
import utils.utils.LogUtil;

import com.mongodb.DB;

/**
 * ClassName:DropDatabase
 * Date:     2015年4月24日 上午10:13:04 
 * @author   sid
 * @see 	 
 */
public class DropDatabase {
	
	private static Logger logger = LogUtil.getInstance().getLogger(DropDatabase.class);
	
	public static void main(String[] args) {
		if ("20150101".compareTo("20150102")==-1) {
			String ss="20150102";
			System.out.println(ss.substring(ss.length()-8, ss.length()));
			System.out.println("20150106".compareTo("20150102"));
		}
	}

	public static void dropDatabase(String now){
		List<String> dbs = MongodbUtil.getInstance().getDBs();
		String[] prefixs = ConfigMongo.DB_DEL_PRE.split(",");
		for (String dbName : dbs) {
			for (String pre : prefixs) {
				if (DataTypeUtil.isNotEmpty(dbName)&&dbName.startsWith(pre)&&dbName.length()>8) {
					String date = dbName.substring(dbName.length()-8, dbName.length());
					//删除今天之前的数据
					if (date.compareTo(now)<0) {
						DB db = MongodbUtil.getInstance().getDB(dbName);
						db.dropDatabase();
						logger.info("删除数据库："+dbName);
					}
				}
			}
		}
	}
}

