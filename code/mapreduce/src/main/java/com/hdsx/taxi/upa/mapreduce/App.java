/**
 * Project Name:mapreduce
 * File Name:App.java
 * Package Name:com.hdsx.taxi.upa.mapreduce
 * Date:2014年12月1日上午9:38:39
 * Copyright (c) 2014, sid Jenkins All Rights Reserved.
 * 
 *
*/

package com.hdsx.taxi.upa.mapreduce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.upa.mapreduce.thread.YyMapReduceThread;

/**
 * ClassName:App
 * Date:     2014年12月1日 上午9:38:39 
 * @author   sid
 * @see 	 
 */
public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) {
		logger.info("启动MapReduce------");
		new YyMapReduceThread("营收启动线程").start();
		logger.info("启动完成MapReduce------");
	}


}

