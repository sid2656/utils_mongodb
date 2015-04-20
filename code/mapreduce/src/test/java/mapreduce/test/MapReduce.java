/**
 * Project Name:PadMain
 * File Name:MapReduce.java
 * Package Name:com.hdsx.ZF.bean.mongo
 * Date:2014年12月1日下午7:59:18
 * Copyright (c) 2014, sid Jenkins All Rights Reserved.
 * 
 *
*/

package mapreduce.test;
/**
 * ClassName:MapReduce
 * Function: TODO ADD FUNCTION. 
 * Reason:	 TODO ADD REASON. 
 * Date:     2014年12月1日 下午7:59:18 
 * @author   sid
 * @see 	 
 */
public class MapReduce {
	private String _id;
	private String value;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public <T> T getObj(Class<T> clazz){
		T obj = null;
		try {
			obj = JsonUtils.getInstance().formJson(this.value, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
}

