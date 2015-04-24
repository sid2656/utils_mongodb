package com.hdsx.taxi.upa.mapreduce.thread;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import org.slf4j.Logger;

import utils.utils.DataTypeUtil;
import utils.utils.DateUtil;
import utils.utils.LogUtil;

import com.hdsx.taxi.upa.mapreduce.bean.TcpUser;
import com.hdsx.taxi.upa.mapreduce.service.TcpUserService;
import com.hdsx.taxi.upa.mapreduce.task.UpdateTask;
import com.hdsx.taxi.upa.mapreduce.utils.MapReduceConfig;
import com.hdsx.taxi.upa.mapreduce.utils.MapReducePropertiesUtil;
import com.hdsx.taxi.upa.mapreduce.utils.MapReduceUtil;

/**
 * 营收MapReduce处理线程
 * ClassName: YyMapReduceThread 
 * date: 2014年12月1日 下午4:48:10 
 *
 * @author sid
 */
public class YyMapReduceThread extends Thread {

	private static final Logger logger = LogUtil.getInstance().getLogger(YyMapReduceThread.class);
	
	public YyMapReduceThread(String name){
		super(name);//调用父类带参数的构造方法
	}
	
	public void run() {
		try {
			this.timerRun();
		} catch (Exception e) {
			logger.error("营收MapReduce处理线程异常", e);
		}
	}

	private void timerRun() {
		//1.小时整点启动
		Calendar cal = Calendar.getInstance();
		logger.debug("整点处理时间："+DateUtil.dateToStr(cal.getTime()));
		int minute = cal.get(Calendar.MINUTE);
		int times = minute/MapReduceConfig.getInterval();
		int min = (times+1)*MapReduceConfig.getInterval();
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.MINUTE, min);
		long startTime = cal.getTimeInMillis();
		cal.add(Calendar.SECOND, MapReduceConfig.getDelay());//整点后多少秒统计
		Timer timer = new Timer("UpdateTask");
		timer.schedule(new UpdateTask(), cal.getTime(),MapReduceConfig.getInterval()*60*1000);
		logger.info("定时整点处理启动完成");
		
		//2.整点前的循环
		logger.info("开始循环处理开始时间到当前时间的相关数据");
		doBeforeMapReduce(cal, startTime);
	}

	/**
	 * 循环做指定时间之前的MapReduce
	 * doBeforeMapReduce:(这里用一句话描述这个方法的作用). 
	 *
	 * @author sid
	 * @param cal
	 * @param startTime
	 */
	private void doBeforeMapReduce(Calendar cal, long startTime) {
		if (DataTypeUtil.isNotEmpty(MapReduceConfig.getYyStart())
				&&!MapReduceConfig.getYyStart().equals(DateUtil.INITTIME)) {
			try {
				Date date = DateUtil.strToDate(MapReduceConfig.getYyStart());
				cal.setTime(date);
				cal.add(Calendar.MINUTE, MapReduceConfig.getInterval());
				Calendar bcal = Calendar.getInstance();
				bcal.setTime(cal.getTime());
				bcal.add(Calendar.DAY_OF_YEAR, -1);
				long loopTime = cal.getTimeInMillis();
				while((loopTime+MapReduceConfig.getInterval()*60*1000)<startTime){
					try {
						String start = DateUtil.dateToStr(cal.getTime());
						String before = DateUtil.dateToStr(bcal.getTime());
						cal.add(Calendar.MINUTE, MapReduceConfig.getInterval());
						String end = DateUtil.dateToStr(cal.getTime());
						logger.debug("开始整点处理：{}--{}",start,end);
						loopTime = cal.getTimeInMillis();
						List<TcpUser> users = TcpUserService.getInstance().getAll();
						for (TcpUser tcpUser : users) {
							try {
								MapReduceUtil.yyMapReduce(tcpUser.getXzqhdm(), String.valueOf(cal.get(Calendar.YEAR)), start, end,before);
							} catch (Exception e) {
								logger.error("定时整点{}处理mapreduce：",tcpUser.getXzqhdm(), e);
								e.printStackTrace();
							}
						}
						logger.info("定时整点处理：{}--{}======完成",start,end);
					} catch (Exception e) {
						logger.error("整点循环{}处理失败：",DateUtil.dateToStr(cal.getTime()), e);
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				logger.error("整点处理失败：", e);
				e.printStackTrace();
			}
		}
	}
}
