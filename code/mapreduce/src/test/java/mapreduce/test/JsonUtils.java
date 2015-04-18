/**
 * Project Name:main
 * File Name:JsonUtils.java
 * Package Name:com.hdsx.taxi.driver.cq.tcp.util
 * Date:2014年3月31日下午3:09:07
 * Copyright (c) 2014, sid Jenkins All Rights Reserved.
 * 
 *
*/

package mapreduce.test;

import com.google.gson.Gson;


/**
 * ClassName:JsonUtils
 * Function: TODO ADD FUNCTION. 
 * Reason:	 TODO ADD REASON. 
 * Date:     2014年3月31日 下午3:09:07 
 * @author   sid
 * @see 	 
 */
public class JsonUtils {

	private volatile static JsonUtils obj = null;
	
	private Gson gson = new Gson();

	public static JsonUtils getInstance() {
		if (obj == null) {
			synchronized (JsonUtils.class) {
				if (obj == null) {
					obj = new JsonUtils();
				}
			}
			obj = new JsonUtils();
		}
		return obj;
	}

	private JsonUtils() {
	}
	
	public String obj2Json(Object obj){
		return JsonUtils.getInstance().getGson().toJson(obj);
	}
	
	public <T> T formJson(String json,Class<T> classOfT){
		return JsonUtils.getInstance().getGson().fromJson(json, classOfT);
	}

	/**
	 * 
	 * getGson:(获取gson实例). 
	 *
	 * @author sid
	 * @return
	 */
	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}
}

