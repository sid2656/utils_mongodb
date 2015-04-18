
package com.hdsx.taxi.upa.mapreduce.service;

import java.util.HashMap;
import java.util.List;

import com.hdsx.taxi.mongo.MongodbUtil;
import com.hdsx.taxi.mongo.init.InitMongo;
import com.hdsx.taxi.upa.mapreduce.bean.TcpUser;

public class TcpUserService {
	
	private volatile static TcpUserService singleton = null;
	private String databasename = InitMongo.DATABASE_INFO;
	private String collectionname = "t_taxi_tcpuser";
	
	public static TcpUserService getInstance() {
		if (singleton == null) {
			synchronized (TcpUserService.class) {
				if (singleton == null) {
					singleton = new TcpUserService();
				}
			}
			singleton = new TcpUserService();
		}
		return singleton;
	}
	
	public TcpUser get(TcpUser bean) {
		List<TcpUser> list = MongodbUtil.getInstance().get(databasename, collectionname, bean.getMapId(), TcpUser.class);
		if (list!=null&&list.size()!=0) {
			return list.get(0);
		}
		return null;
	}

	public List<TcpUser> getByProperty(HashMap<String, Object> map) {
		return MongodbUtil.getInstance().get(databasename, collectionname, map, TcpUser.class);
	}

	public List<TcpUser> getByCondition(
			HashMap<String, HashMap<String, Object>> map) {
		return MongodbUtil.getInstance().getByCondition(databasename, collectionname, map, TcpUser.class);
	}

	public List<TcpUser> getAll() {
		return MongodbUtil.getInstance().getAll(databasename, collectionname, TcpUser.class);
	}
}
