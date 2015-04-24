package com.hdsx.taxi.upa.mapreduce.task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.slf4j.Logger;

import utils.utils.DateUtil;
import utils.utils.LogUtil;

import com.hdsx.taxi.upa.mapreduce.bean.TcpUser;
import com.hdsx.taxi.upa.mapreduce.service.TcpUserService;
import com.hdsx.taxi.upa.mapreduce.utils.MapReduceConfig;
import com.hdsx.taxi.upa.mapreduce.utils.MapReduceUtil;

public class UpdateTask extends TimerTask {

	private static final Logger logger = LogUtil.getInstance().getLogger(UpdateTask.class);
	
	
	public UpdateTask() {
		
	}


	@Override
	public void run() {
		Calendar cal = Calendar.getInstance();
		try {
			logger.debug("开始整点处理时间：{}--{}");
			cal.setTime(new Date(scheduledExecutionTime()));
			cal.add(Calendar.SECOND, -MapReduceConfig.getDelay());
			String end = DateUtil.dateToStr(cal.getTime());
			cal.add(Calendar.MINUTE, -MapReduceConfig.getInterval());
			String start = DateUtil.dateToStr(cal.getTime());
			Calendar bcal = Calendar.getInstance();
			bcal.setTime(cal.getTime());
			bcal.add(Calendar.DAY_OF_YEAR, -1);
			String before = DateUtil.dateToStr(bcal.getTime());
			
			List<TcpUser> users = TcpUserService.getInstance().getAll();
			for (TcpUser tcpUser : users) {
				try {
					MapReduceUtil.yyMapReduce(tcpUser.getXzqhdm(), String.valueOf(cal.get(Calendar.YEAR)), start, end,before);
				} catch (Exception e) {
					logger.error("定时整点{}处理mapreduce：",tcpUser.getXzqhdm(), e);
					e.printStackTrace();
				}
			}
			
			logger.info("定时整点处理时间：{}--{}======完成",start,end);
		} catch (Exception e) {
			logger.error("定时整点处{}处理失败：",DateUtil.dateToStr(cal.getTime()), e);
			e.printStackTrace();
		}
	}

}
