/**
 * Project Name:mapreduce
 * File Name:TimeTest.java
 * Package Name:mapreduce.test
 * Date:2014年12月1日下午5:55:01
 * Copyright (c) 2014, sid Jenkins All Rights Reserved.
 * 
 *
*/

package mapreduce.test;

import com.hdsx.taxi.upa.mapreduce.utils.DateUtil;

/**
 * ClassName:TimeTest
 * Function: TODO ADD FUNCTION. 
 * Reason:	 TODO ADD REASON. 
 * Date:     2014年12月1日 下午5:55:01 
 * @author   sid
 * @see 	 
 */
public class TimeTest {

	public static void main(String[] args) {
		String time = "20141201121600";
		System.out.println(DateUtil.strToDate(time).getTime());
	}
}

