package com.hdsx.taxi.upa.mapreduce.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@SuppressWarnings({ "rawtypes", "unchecked","static-access" })
public final class DateUtil {
	public static Logger logger = LoggerFactory.getLogger(DateUtil.class);
    static Calendar cld;
    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static String INITTIME = "00000000000000";


    /**
     *  yql add .... 取得时间类对象Calendar
     *
     *@return
     */
    public static Calendar getCalendar() {
        return getCalendar(null);
    }


    /**
     *  yql add .... 根据java.util.Date 对象取得 Calendar对象
     *
     *@param  date  时间对象java.util.Date
     *@return       时间对象 Calendar
     */
    public static Calendar getCalendar(java.util.Date date) {
        if (cld == null) {
            cld = new GregorianCalendar();
        }
        if (date != null) {
            cld.setTime(date);
        }
        return cld;
    }
    
    public static Calendar getCalendarNew() {
        return getCalendarNew(null);
    }


    /**
     *  yql add .... 根据java.util.Date 对象取得 Calendar对象
     *
     *@param  date  时间对象java.util.Date
     *@return       时间对象 Calendar
     */
    public static Calendar getCalendarNew(java.util.Date date) {
        GregorianCalendar gcld = new GregorianCalendar();
        if (date != null) {
            gcld.setTime(date);
        }
        return gcld;
    }


    /**
     *  取得本月第一天的日期
     *
     *@param  DQRQ
     *@return
     */
    public static String getCurMonthFirstDay(String DQRQ) {
        String Year = DQRQ.substring(0, 4);
        String Month = DQRQ.substring(5, 7);
        String Day = "01";
        String strReturn = Year + "-" + Month + "-" + Day;
        return strReturn;
    }


    /**
     *  yql add .... 取得当前月的最大天数
     *
     *@return
     */
    public static int getDaysOfMonth() {
        return getDaysOfMonth(getRightYear(), getRightMonth());
    }


    /**
     *  yql add .... 取得一个月的最大天数
     *
     *@param  year
     *@param  month
     *@return
     */
    public static int getDaysOfMonth(int year, int month) {
        return (int) ((toLongTime(month == 12 ? (year + 1) : year, month == 12 ? 1 : (month + 1), 1) - toLongTime(year, month, 1)) / (24 * 60 * 60 * 1000));
    }



    /**
     *  yql add .... 取得下一个月的最大天数
     *
     *@param  year
     *@param  month
     *@return
     */
    public static int getDaysOfNextMonth(int year, int month) {
        year = month == 12 ? year + 1 : year;
        month = month == 12 ? 1 : month + 1;
        return getDaysOfMonth(year, month);
    }


    /**
     *  yql add .... 取得上个月的最大天数
     *
     *@param  year
     *@param  month
     *@return
     */
    public static int getDaysOfProMonth(int year, int month) {
        year = month == 1 ? year - 1 : year;
        month = month == 1 ? 12 : month - 1;
        return getDaysOfMonth(year, month);
    }


    /**
     *  取上月第一天
     *
     *@param  dqrq  Description of Parameter
     *@return       String
     *@since        2003-0416
     */
    public static String getFirstDayOfPreMonth(String dqrq) {
        String strDQRQ = dqrq;
        String strYear = "";
        String strMonth = "";
        String strDay = "";
        if (strDQRQ == null) {
            return "";
        }
        if (strDQRQ.length() == 8) {
            strYear = strDQRQ.substring(0, 4);
            strMonth = strDQRQ.substring(4, 6);
            strDay = strDQRQ.substring(6, 8);
        }
        if (strDQRQ.length() == 10) {
            strYear = strDQRQ.substring(0, 4);
            strMonth = strDQRQ.substring(5, 7);
            strDay = strDQRQ.substring(8, 10);
        }

        int iMonth = toInt(strMonth);
        int iYear = toInt(strYear);
        if (iMonth == 1) {
            iYear = iYear - 1;
            iMonth = 12;
        } else if (iMonth > 1) {
            iMonth = iMonth - 1;
        } else {
            return "";
        }
        if (iMonth < 10) {
            strMonth = "0" + iMonth;
        } else {
            strMonth = "" + iMonth;
        }

        strDay = "01";
        if (strDQRQ.length() == 8) {
            return iYear + strMonth + strDay;
        } else if (strDQRQ.length() == 10) {
            return iYear + "-" + strMonth + "-" + strDay;
        } else {
            return "";
        }
    }

    /**
     * add by 岳潜龙
     * 取当前月的最后一个工作日 也就是出去礼拜六、礼拜天的最后一天的日期
     * @return
     */
    public static Date getLastWorkDayOfRightMonth()
    {
      String strTest=getPreMonthMaxDay(getNextMonthLastDay(getStringDate("yyyy-MM-dd")));
      Date testDate=strToDate(strTest);
      Calendar cld=GregorianCalendar.getInstance();
      cld.setTime(testDate);
      int testa=cld.get(cld.DAY_OF_WEEK);
      if(testa!=1&&testa!=7)
      {
        return cld.getTime();
      }else if(testa==1)
      {
        cld.set(cld.get(cld.YEAR),cld.get(cld.MONTH),cld.get(cld.DATE)-2);
        return cld.getTime();
      }else if(testa==7)
      {
        cld.set(cld.get(cld.YEAR),cld.get(cld.MONTH),cld.get(cld.DATE)-1);
        return cld.getTime();
      }
      return cld.getTime();
    }


  /**
      *  The main program for the DateUtil class
      *
      *@param  args  The command line arguments
      */
     public static void main(String[] args) {
       String test=dateToShortStr(getLastWorkDayOfRightMonth());
         System.out.println(test);
     }

    /**
     *  Gets the LastDate attribute of the DateUtil class
     *
     *@param  day  Description of Parameter
     *@return      The LastDate value
     */
    public static Date getLastDate(long day) {
        Date date = new Date();
        long date_3_hm = date.getTime() - 3600000 * 34 * day;
        Date date_3_hm_date = new Date(date_3_hm);
        return date_3_hm_date;
    }


    /**
     *  Gets the LongStringDate attribute of the DateUtil class
     *
     *@return    The LongStringDate value
     */
    public static String getLongStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:ms");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    //add by hejiang 20030804
    /**
     *  取得下个月第一天的日期
     *
     *@param  DQRQ
     *@return
     */
    public static String getNextMonthFirstDay(String DQRQ) {
        String Year = DQRQ.substring(0, 4);
        int intYear = Integer.valueOf(Year).intValue();
        String Month = DQRQ.substring(5, 7);
        int intMonth = Integer.valueOf(Month).intValue();
        String Day = "01";
        if (intMonth == 12) {
            intYear = intYear + 1;
            intMonth = 1;
        } else {
            intMonth = intMonth + 1;
        }
        Year = String.valueOf(intYear);
        Month = String.valueOf(intMonth);
        if (Month.length() == 1) {
            Month = "0" + Month;
        }
        String strReturn = Year + "-" + Month + "-" + Day;
        return strReturn;
    }


    /**
     *  取得下个月第一天的日期
     *
     *@param  DQRQ
     *@return
     */
    public static String getNextMonthLastDay(String DQRQ) {
        String firstDay = getNextMonthFirstDay(DQRQ);
        String Year = firstDay.substring(0, 4);
        int intYear = Integer.valueOf(Year).intValue();
        String Month = firstDay.substring(5, 7);
        int intMonth = Integer.valueOf(Month).intValue();
        int days = getDaysOfMonth(intYear, intMonth);
        String strReturn = firstDay.substring(0, 8) + days;
        return strReturn;
    }


    /**
     *  根据当前日期取得 下一个月报所属期
     *
     *@param  date  当前日期
     *@return
     */
    public static Date[] getNextMonths(Date date) {
        int iYear = getRightYear(date);
        int iMonth = getRightMonth(date);
        Date[] dates = new Date[2];
        Calendar cld = GregorianCalendar.getInstance();
        cld.set(iYear, iMonth - 1, 1);
        dates[0] = cld.getTime();
        cld.set(iYear, iMonth - 1, getDaysOfMonth(iYear, iMonth));
        dates[1] = cld.getTime();
        return dates;
    }


    /**
     *  根据当前日期 取得下一个季报所属期 所属期
     *
     *@param  date
     *@return
     */
    public static Date[] getNextQuarters(Date date) {
        int iYear = getRightYear(date);
        int iMonth = getRightMonth(date);
        Calendar cld = GregorianCalendar.getInstance();
        Date[] dates = new Date[2];
        if (iMonth >= 1 && iMonth < 4) {
            cld.set(iYear, 0, 1);
            dates[0] = cld.getTime();
            cld.set(iYear, 2, 31);
            dates[1] = cld.getTime();
        } else if (iMonth >= 4 && iMonth < 7) {
            cld.set(iYear, 3, 1);
            dates[0] = cld.getTime();
            cld.set(iYear, 5, 30);
            dates[1] = cld.getTime();
        } else if (iMonth >= 7 && iMonth < 10) {
            cld.set(iYear, 6, 1);
            dates[0] = cld.getTime();
            cld.set(iYear, 8, 30);
            dates[1] = cld.getTime();
        } else {
            cld.set(iYear, 9, 1);
            dates[0] = cld.getTime();
            cld.set(iYear, 11, 31);
            dates[1] = cld.getTime();
        }
        return dates;
    }


    /**
     *  Gets the nextSomeDay attribute of the DateUtil class
     *
     *@param  strDate  Description of the Parameter
     *@param  next     Description of the Parameter
     *@return          The nextSomeDay value
     */
    public static String getNextSomeDay(String strDate, int next) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date strtodate = formatter.parse(strDate);
            long tomorrowLong = strtodate.getTime() + next * 24 * 60 * 60 * 1000;
            System.err.println(tomorrowLong);
            Date tomorrow_date = new Date(tomorrowLong);
            return formatter.format(tomorrow_date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     *  根据当前日期取得 下一个年报所属期
     *
     *@param  date
     *@return
     */
    public static Date[] getNextYears(Date date) {
        Date[] dates = new Date[2];
        int iYear = getRightYear(date);
        Calendar cld = GregorianCalendar.getInstance();

        cld.set(iYear, 0, 1);
        dates[0] = cld.getTime();
        cld.set(iYear, 11, 31);
        dates[1] = cld.getTime();
        return dates;
    }


    /**
     *  Gets the Now attribute of the DateUtil class
     *
     *@return    The Now value
     */
    public static Date getNow() {
        Date currentTime = new Date();
        return currentTime;
    }


    /**
     *  Gets the NowDate attribute of the DateUtil class
     *
     *@return    The NowDate value
     */
    public static Date getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }


    /**
     *  Gets the NowDateShort attribute of the DateUtil class
     *
     *@return    The NowDateShort value
     */
    public static Date getNowDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }
    
    


    /**
     *  将字符串转化为ORACLE所要求的日期字符串格式 <P>
     *
     *  ADD BY WJZ 031229<P>
     *
     *
     *
     *@param  date  日期字符串
     *@return       ORACLE所要求的日期字符串格式
     */
    public static String getOracleDateFormat(String date) {
        if (date == null) {
            return null;
        }
        if (date.length() == 19) {
            if (date.indexOf("-") > 0) {
                return "to_date('" + date + "','YYYY-MM-DD HH24:MI:SS')";
            } else if (date.indexOf("/") > 0) {
                return "to_date('" + date + "','YYYY/MM/DD HH24:MI:SS')";
            } else {
                return date;
            }
        } else if (date.length() == 18) {
            if (date.indexOf("-") > 0) {
                return "to_date('" + date + "','YYYY-MM-DDHH24:MI:SS')";
            } else if (date.indexOf("/") > 0) {
                return "to_date('" + date + "','YYYY/MM/DDHH24:MI:SS')";
            } else {
                return date;
            }
        } else if (date.length() == 14) {

            return "to_date('" + date + "','YYYYMMDDHH24MISS')";
        } else if (date.length() == 10) {
            if (date.indexOf("-") > 0) {
                return "to_date('" + date + "','YYYY-MM-DD')";
            } else if (date.indexOf("/") > 0) {
                return "to_date('" + date + "','YYYY/MM/DD')";
            } else {
                return date;
            }
        } else if (date.length() == 8) {
            return "to_date('" + date + "','YYYYMMDD')";
        } else {
            return date;
        }

    }
    
    public static String getOracleDateFormat(Date d) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return getOracleDateFormat(sf.format(d));
    }


    /**
     *  取上月最后一天
     *
     *@param  str  Description of Parameter
     *@return      String
     *@since       2003-0416
     */
    public static String getPreDate(String str) {
        String strRQ = str;
        java.text.SimpleDateFormat df = null;
        if (str == null) {
            return "";
        }
        if (str.length() == 10) {
            df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        } else if (str.length() == 8) {
            df = new java.text.SimpleDateFormat("yyyyMMdd");
        } else {
            return "";
        }
        try {
            java.util.Date dt = df.parse(strRQ);
            long lg = dt.getTime() - 24 * 60 * 60 * 1000;
            dt.setTime(lg);
            return df.format(dt);
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            return "";
        }
    }


    /**
     *  取上月最后一天
     *
     *@param  dqrq  Description of Parameter
     *@return       String
     *@since        2003-0416
     */
    public static String getPreMaxDayOfMonth(String dqrq) {
        String strDQRQ = dqrq;
        String strReturn = "";
        String strYear = "";
        String strMonth = "";
        String strDay = "";
        if (strDQRQ == null) {
            return "";
        }
        if (strDQRQ.length() == 8) {
            strYear = strDQRQ.substring(0, 4);
            strMonth = strDQRQ.substring(4, 6);
            strDay = strDQRQ.substring(6, 8);
        }
        if (strDQRQ.length() == 10) {
            strYear = strDQRQ.substring(0, 4);
            strMonth = strDQRQ.substring(5, 7);
            strDay = strDQRQ.substring(8, 10);
        }

        strDay = "01";
        if (strDQRQ.length() == 8) {
            strDQRQ = strYear + strMonth + strDay;
        }
        if (strDQRQ.length() == 10) {
            strDQRQ = strYear + "-" + strMonth + "-" + strDay;
        }
        strReturn = getPreDate(strDQRQ);
        if (strReturn == null) {
            strReturn = "";
        }
        return strReturn;
    }


    /**
     *  上月第一天
     *
     *@param  dqrq
     *@return
     */
    public static String getPreMonthFirstDay(String dqrq) {
        return getFirstDayOfPreMonth(dqrq);
    }


    /**
     *  上月最后一天
     *
     *@param  dqrq
     *@return
     */
    public static String getPreMonthMaxDay(String dqrq) {
        return getPreMaxDayOfMonth(dqrq);
    }


    /**
     *  Gets the RightDate attribute of the DateUtil class
     *
     *@return    The RightDate value
     */
    public static int getRightDate() {
        return getRightDate((Date) null);
    }


    /**
     *  Gets the RightDate attribute of the DateUtil class
     *
     *@param  date  Description of Parameter
     *@return       The RightDate value
     */
    public static int getRightDate(Date date) {
        Calendar rightMonth = Calendar.getInstance();
        if (date != null) {
            rightMonth.setTime(date);
        }
        return rightMonth.get(Calendar.DATE);
    }


    /**
     *  Gets the RightDate attribute of the DateUtil class
     *
     *@param  date  Description of Parameter
     *@return       The RightDate value
     */
    public static int getRightDate(Calendar date) {
        if (date == null) {
            return getRightDate((Date) null);
        } else {
            return getRightDate(date.getTime());
        }
    }


    /**
     *  Gets the RightHour attribute of the DateUtil class
     *
     *@return    The RightHour value
     */
    public static int getRightHour() {
        Calendar rightHour = Calendar.getInstance();
        return rightHour.get(Calendar.HOUR);
    }


    /**
     *  Gets the RightMinute attribute of the DateUtil class
     *
     *@return    The RightMinute value
     */
    public static int getRightMinute() {
        Calendar rightMinute = Calendar.getInstance();
        return rightMinute.get(Calendar.MINUTE);
    }



    /**
     *  Gets the RightMonth attribute of the DateUtil class
     *
     *@return    The RightMonth value
     */
    public static int getRightMonth() {
        return getRightMonth((Date) null);
    }


    /**
     *  Gets the RightMonth attribute of the DateUtil class
     *
     *@param  date  Description of Parameter
     *@return       The RightMonth value
     */
    public static int getRightMonth(Date date) {
        Calendar rightMonth = Calendar.getInstance();
        if (date != null) {
            rightMonth.setTime(date);
        }
        return rightMonth.get(Calendar.MONTH) + 1;
    }


    /**
     *  Gets the RightMonth attribute of the DateUtil class
     *
     *@param  date  Description of Parameter
     *@return       The RightMonth value
     */
    public static int getRightMonth(Calendar date) {
        if (date == null) {
            return getRightMonth((Date) null);
        } else {
            return getRightMonth(date.getTime());
        }

    }


    /**
     *  Gets the RightSecond attribute of the DateUtil class
     *
     *@return    The RightSecond value
     */
    public static int getRightSecond() {
        Calendar rightSecond = Calendar.getInstance();
        return rightSecond.get(Calendar.SECOND);
    }


    /**
     *  以上这六个函数是分别取得当前的年、月、日、时、分、妙
     *
     *@param  isTime  Description of Parameter
     *@return         The RightTimes value
     */

    /**
     *  以上这六个函数是分别取得当前的年、月、日、时、分、妙 以上这六个函数是分别取得当前的年、月、日、时、分、妙
     *  以上这六个函数是分别取得当前的年、月、日、时、分、妙 以上这六个函数是分别取得当前的年、月、日、时、分、妙
     *  以上这六个函数是分别取得当前的年、月、日、时、分、妙 以上这六个函数是分别取得当前的年、月、日、时、分、妙
     *  以上这六个函数是分别取得当前的年、月、日、时、分、妙 取得当前的时间，以数组的形式返回
     *
     *@param  isTime  Description of Parameter
     *@return         The RightTimes value
     *@return         The RightTimes value
     *@return         The RightTimes value
     *@return         The RightTimes value
     *@return         The RightTimes value
     *@return         The RightTimes value
     *@return         The RightTimes value
     *@return
     */
    public static int[] getRightTimes(boolean isTime) {
        Calendar rightTime = Calendar.getInstance();
        int time[];
        if (isTime) {
            time = new int[6];
            time[0] = rightTime.get(Calendar.YEAR);
            time[1] = rightTime.get(Calendar.MONTH) + 1;
            time[2] = rightTime.get(Calendar.DATE);
            time[3] = rightTime.get(Calendar.HOUR);
            time[4] = rightTime.get(Calendar.MINUTE);
            time[5] = rightTime.get(Calendar.SECOND);
        } else {
            time = new int[3];
            time[0] = rightTime.get(Calendar.YEAR);
            time[1] = rightTime.get(Calendar.MONTH) + 1;
            time[2] = rightTime.get(Calendar.DATE);
        }
        return time;
    }


    /**
     *  取得当前的年 以整数形式返回， 例如：1999
     *
     *@return    返回当前年
     */
    public static int getRightYear() {
        return getRightYear((Date) null);
    }


    /**
     *  根据Date对象取得 年份 整数形式
     *
     *@param  date  java.util.Date 对象
     *@return       返回 java.util.Date 的年份
     */
    public static int getRightYear(Date date) {
        Calendar rightYear = Calendar.getInstance();
        if (date != null) {
            rightYear.setTime(date);
        }
        return rightYear.get(Calendar.YEAR);
    }


    /**
     *  根据Calendar 对象返回 年份 整数形式
     *
     *@param  date  Calendar 对象
     *@return       Calendar 的年份
     */
    public static int getRightYear(Calendar date) {
        if (date != null) {
            return getRightYear(date.getTime());
        } else {
            return getRightYear((Date) null);
        }
    }


    /**
     *  Gets the S attribute of the DateUtil class
     *
     *@param  strDate  Description of Parameter
     *@return          The S value
     */
    public static long getS(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate.getTime();
    }


    //add by hejiang 20030611
    /**
     *  Gets the SN attribute of the DateUtil class
     *
     *@param  strDate  Description of Parameter
     *@return          The SN value
     */
    public static long getSN(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate.getTime();
    }


    //end by add

    //add by hejiang 20030703
    /**
     *  Gets the SkQData attribute of the DateUtil class
     *
     *@param  SSSQ  Description of Parameter
     *@return       The SkQData value
     */
    public static long getSkQData(String SSSQ) {
        return Long.valueOf(SSSQ + "01").longValue();
    }


    /**
     *  Gets the SkZData attribute of the DateUtil class
     *
     *@param  SSSQ  Description of Parameter
     *@return       The SkZData value
     */
    public static long getSkZData(String SSSQ) {
        int year = Integer.valueOf(SSSQ.substring(0, 4)).intValue();
        int month = Integer.valueOf(SSSQ.substring(4, 6)).intValue();
        int day = getDaysOfMonth(year, month);
        String strDay;
        if (day < 10) {
            strDay = "0" + String.valueOf(day);
        } else {
            strDay = String.valueOf(day);
        }
        return Long.valueOf(SSSQ + strDay).longValue();
    }


    /**
     *  Gets the SqDate attribute of the DateUtil class
     *
     *@param  SSSQ_QZ  Description of Parameter
     *@return          The SqDate value
     */
    public static long getSqDate(String SSSQ_QZ) {
        return Long.valueOf(SSSQ_QZ.substring(0, 6)).longValue();
    }


    /**
     *  Gets the SqlDate attribute of the DateUtil class
     *
     *@return    The SqlDate value
     */
    public static java.sql.Date getSqlDate() {
        return new java.sql.Date(new Date().getTime());
    }
    
    public static java.sql.Timestamp getSqlTimestamp() {
        return new java.sql.Timestamp(new Date().getTime());
    }


    /**
     *  Gets the SqlDate attribute of the DateUtil class
     *
     *@param  d  Description of Parameter
     *@return    The SqlDate value
     */
    public static java.sql.Date getSqlDate(String d) {
        java.util.Date dd = strToDate(d);
        return new java.sql.Date(dd.getTime());
    }


    /**
     *  Gets the StringDate attribute of the DateUtil class
     *
     *@return    The StringDate value
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    /**
     *  Gets the StringDate attribute of the DateUtil class
     *
     *@param  format  Description of Parameter
     *@return         The StringDate value
     */
    public static String getStringDate(String format) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    /**
     *  Gets the StringDateShort attribute of the DateUtil class
     *
     *@return    The StringDateShort value
     */
    public static String getStringDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    
    /**
     * 取得今天的日期加最小时间
     * @return
     */
    public static Date getTodayMin() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date();
        String ds = f.format(d);
        try {
            return f.parse(ds);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 取得今天的日期加最大时间
     * @return
     */
    public static Date getTodayMax() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date();
        String ds = f.format(d) + " 23:59:59";
        try {
            f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return f.parse(ds);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //end by add

    //add by hejiang 20030824
    /**
     *  Gets the tomorrow attribute of the DateUtil class
     *
     *@param  strDate  Description of the Parameter
     *@return          The tomorrow value
     */
    public static String getTomorrow(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date strtodate = formatter.parse(strDate);
            long tomorrowLong = strtodate.getTime() + 24 * 60 * 60 * 1000;
            System.err.println(tomorrowLong);
            Date tomorrow_date = new Date(tomorrowLong);
            return formatter.format(tomorrow_date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //end by add

    //add by hejiang 20030811
    /**
     *  Gets the yesterDay attribute of the DateUtil class
     *
     *@param  strDate  Description of the Parameter
     *@return          The yesterDay value
     */
    public static String getYesterDay(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date strtodate = formatter.parse(strDate);
            long yesterLong = strtodate.getTime() - 24 * 60 * 60 * 1000;
            Date yester_date = new Date(yesterLong);
            return formatter.format(yester_date);
        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }
    }


    /**
     * add by yql
     *  校验日期是否合法！
     *
     *@param  strDate  日期字符串，例如2003-03-05或者20030507;
     *@return          合法，返回true;非法，返回false
     */
    public static boolean isVerifyDate(String strDate) {
        if (strDate == null || (strDate.trim().length() != 10 && strDate.trim().length() != 8)) {
            return false;
        }
        int iYear = 0;
        int iMonth = 0;
        int iDay = 0;
        boolean dateLenFlags = strDate.trim().length() == 10;
        try {
            if (dateLenFlags) {
                iYear = Integer.parseInt(strDate.substring(0, 4));
                iMonth = Integer.parseInt(strDate.substring(5, 7));
                iDay = Integer.parseInt(strDate.substring(8, 10));
            } else {
                iYear = Integer.parseInt(strDate.substring(0, 4));
                iMonth = Integer.parseInt(strDate.substring(4, 6));
                iDay = Integer.parseInt(strDate.substring(6, 8));
            }

        } catch (Exception e) {
            return false;
        }
        if (iMonth > 12 || iMonth < 1) {
            return false;
        } else
                if (iDay > getDaysOfMonth(iYear, iMonth)) {
            return false;
        } else {
            return true;
        }
    }


    /**
     *  取下一属期的起止日期 return Vector
     *
     *@param  DQRQ  Description of Parameter
     *@return       Description of the Returned Value
     */
    public static Vector ChangeDate(String DQRQ) {
        Vector vcdate = new Vector();
        Date date = DateUtil.strToBirthday(DQRQ);
        Date[] nextmonth = DateUtil.getNextMonths(date);
        Date[] nextyear = DateUtil.getNextYears(date);
        Date[] nextquarter = DateUtil.getNextQuarters(date);
        vcdate.addElement(DateUtil.dateToString(nextmonth[0]));
        vcdate.addElement(DateUtil.dateToString(nextmonth[1]));
        vcdate.addElement(DateUtil.dateToString(nextquarter[0]));
        vcdate.addElement(DateUtil.dateToString(nextquarter[1]));
        vcdate.addElement(DateUtil.dateToString(nextyear[0]));
        vcdate.addElement(DateUtil.dateToString(nextyear[1]));
        return vcdate;
    }


    //add by hejiang 20030613
    /**
     *  Description of the Method
     *
     *@param  DQRQ  Description of Parameter
     *@return       Description of the Returned Value
     */
    public static String changeDate(String DQRQ) {
        String strDQRQ = DQRQ;
        String strYear = "";
        String strMonth = "";
        String strDay = "";
        strYear = strDQRQ.substring(0, 4);
        strMonth = strDQRQ.substring(5, 7);
        strDay = strDQRQ.substring(8, 10);
        return strYear + strMonth + strDay;
    }


    /**
     *  转换Date（字符表示）的表示方法，例如将yyyy-mm-dd转换为yyyy/mm/dd<p>
     *
     *  add by wjz 0326
     *
     *@param  format  Description of Parameter
     *@param  date    Description of the Parameter
     *@return         The StringDate value
     */
    public static String convertDateFormat(String date, String format) {
        Date currentTime = strToDate(date);
        if (currentTime == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    /**
     *  Description of the Method
     *
     *@param  dateDate  Description of Parameter
     *@return           Description of the Returned Value
     */
    public static String dateToShortStr(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }


    /**
     *  Description of the Method
     *
     *@param  dateDate  Description of Parameter
     *@return           Description of the Returned Value
     */
    public static String dateToStr(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(YYYYMMDDHHMMSS);
        String dateString = formatter.format(dateDate);
        return dateString;
    }


    /**
     *  转换日期为字符串格式 <p>
     *
     *  add by wjz 1125
     *
     *@param  dateDate
     *@param  format
     *@return
     */
    public static String dateToStr(java.util.Date dateDate, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            String dateString = formatter.format(dateDate);
            return dateString;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return "";
        }

    }


	/**
	 * 将对象转换为整数型
	 * 
	 * @param o
	 *            源对象
	 * @return 对应的Int值,如果出错,则返回Integer.MIN_VALUE
	 */
	public static int toInt(Object o) {
		if (o == null) {
			throw new IllegalArgumentException("该对象为空");
		}
		String s = o.toString();
		try {
			return Integer.parseInt(s);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return Integer.MIN_VALUE;
		}
	}

    /**
     *  将Date日期格式转换为字符串格式，不代时分秒格式
     *
     *@param  date
     *@return
     */
    public final static String dateToString(Date date) {
        return dateToString(date, false);
    }


    /**
     *  将date类型转换成字符串，
     *
     *@param  date
     *@param  bHaveHour  是否返回带有时分秒格式
     *@return            例如yyyy-mm-dd或者yyyy-mm-dd hh:mm:ss
     */
    public final static String dateToString(java.util.Date date, boolean bHaveHour) {
        Calendar cal = getCalendarNew(date);
        if (!bHaveHour || (cal.get(Calendar.HOUR_OF_DAY) == 0 && cal.get(Calendar.MINUTE) == 0 && cal.get(Calendar.SECOND) == 0)) {
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            return "" + year + (month < 10 ? "-0" : "-") + month + (day < 10 ? "-0" : "-") + day;
        }
        return date.toString();
    }


    /**
     *  计算两个日期之间相隔的天数
     *
     *@param  date1  Description of Parameter
     *@param  date2  Description of Parameter
     *@return        返回相隔的天数
     */
    public static int diffDate(Calendar date1, Calendar date2) {
        return diffDate(date1.getTime(), date2.getTime());
    }


    /**
     *  计算两个日期之间相隔的天数
     *
     *@param  date1  Description of Parameter
     *@param  date2  Description of Parameter
     *@return        返回相隔的天数
     */
    public static int diffDate(java.util.Date date1, java.util.Date date2) {
        return (int) ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000));
    }


    /**
     *  计算两个日期之间相隔的天数
     *
     *@param  date1  Description of Parameter
     *@param  date2  Description of Parameter
     *@return        Description of the Returned Value
     */
    public static int diffDate(String date1, String date2) {
        return diffDate(strToDate(date1), strToDate(date2));
    }


    /**
     *  yql添加 求某一天加上或者减去几天（当day为负数的时候）的日期
     *
     *@param  date  某一天
     *@param  day   天数
     *@return       返回的结果日期
     */
    public static java.util.Date incDate(java.util.Date date, int day) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, day);
        return cal.getTime();
    }



    /**
     *  yql 添加
     *
     *@param  format   String
     *@param  day      int
     *@param  strDate  Description of the Parameter
     *@return          String
     */
    public static String incDate(String strDate, String format, int day) {
        Date date = strToDate(strDate);
        Date rtnDate = incDate(date, day);
        return dateToStr(rtnDate, format);
    }


    /**
     *@param  date
     *@param  month
     *@return
     */
    public static java.util.Date incMonth(java.util.Date date, int month) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.MONTH, month);
        return cal.getTime();
    }


    /**
     *@param  date
     *@param  year
     *@return
     */
    public static java.util.Date incYear(java.util.Date date, int year) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.YEAR, year);
        return cal.getTime();
    }





    /**
     *  Description of the Method
     *
     *@param  strDate  Description of Parameter
     *@return          Description of the Returned Value
     */
    public static Date strToBirthday(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }


    /**
     *  将字符串格式的日期转换为Date型的日期<p>
     *
     *  modify by wjz 0326<p>
     *
     *  考虑到日期格式比较复杂，在转换之前先做如下假定：<p>
     *
     *  都是按照年－月－日的格式排列<p>
     *
     *  年都是4位<p>
     *
     *
     *
     *@param  strDate  Description of Parameter
     *@return          Description of the Returned Value
     */
    public static Date strToDate(String strDate) {
        if (strDate == null || strDate.length() < 6) {
            throw new IllegalArgumentException("illeage date format");
        }
        String fmt = "yyyy-MM-dd HH:mm:ss";
//        if (strDate.length() == 10) {
//            fmt = "yyyy-MM-dd";
//        } else if (strDate.length() == 8) {
//            if (strDate.indexOf("-") > 0) {
//                fmt = "yy-MM-dd";
//            } else {
//                fmt = "yyyyMMdd";
//            }
//        } else if (strDate.length() == 6) {
//            fmt = "yyMMdd";
//        }
        if (strDate.length() == 19) {
            if (strDate.indexOf("-") > 0) {
                fmt = "yyyy-MM-dd HH:mm:ss";
            } else if (strDate.indexOf("/") > 0) {
                fmt = "yyyy/MM/dd HH:mm:ss";
            }
        } else if (strDate.length() == 18) {
            if (strDate.indexOf("-") > 0) {
                fmt = "yyyy-MM-ddHH:mm:ss";
            } else if (strDate.indexOf("/") > 0) {
                fmt = "yyyy/MM/dd HH:mm:ss";
            }
        } else if (strDate.length() == 14) {

            fmt = "yyyyMMddHHmmss";
        } else if (strDate.length() == 10) {
            if (strDate.indexOf("-") > 0) {
                fmt = "yyyy-MM-dd";
            } else if (strDate.indexOf("/") > 0) {
                fmt = "yyyy/MM/dd";
            } else if (strDate.indexOf(".") > 0) {
                fmt = "yyyy.MM.dd";
            }
        } else if (strDate.length() == 8) {
            if (strDate.indexOf("-") > 0) {
                fmt = "yy-MM-dd";
            } else if (strDate.indexOf("/") > 0) {
                fmt = "yy/MM/dd";
            } else if (strDate.indexOf(".") > 0) {
                fmt = "yy.MM.dd";
            } else {
                fmt = "yyyyMMdd";
            }

        }

        SimpleDateFormat formatter = new SimpleDateFormat(fmt);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }


    /**
     *  从年月日得到一个Date对象
     *
     *@param  year   年
     *@param  month  月
     *@param  day    日
     *@return        得到的Date对象
     */
    public static java.util.Date toDate(int year, int month, int day) {
        if (cld == null) {
            cld = new GregorianCalendar();
        }
        cld.clear();
        cld.set(Calendar.YEAR, year);
        cld.set(Calendar.MONTH, month - 1);
        cld.set(Calendar.DAY_OF_MONTH, day);
        return cld.getTime();
        //.getTime();
    }


    /**
     *  从给定的 year,mongth,day 得到时间的long值表示(a point in time that is <tt>time</tt>
     *  milliseconds after January 1, 1970 00:00:00 GMT).
     *
     *@param  year   年
     *@param  month  月
     *@param  day    日
     *@return        给定的 year,mongth,day 得到时间的long值表示
     */
    public static long toLongTime(int year, int month, int day) {
        return toDate(year, month, day).getTime();
    }


    /**
     *  Description of the Method
     *
     *@param  year   Description of Parameter
     *@param  month  Description of Parameter
     *@param  day    Description of Parameter
     *@param  hour   Description of Parameter
     *@param  min    Description of Parameter
     *@param  sec    Description of Parameter
     *@return        Description of the Returned Value
     */
    public static long toLongTime(int year, int month, int day, int hour, int min, int sec) {
        if (cld == null) {
            cld = new GregorianCalendar();
        }
        cld.clear();
        cld.set(Calendar.YEAR, year);
        cld.set(Calendar.MONTH, month - 1);
        cld.set(Calendar.DAY_OF_MONTH, day);
        cld.set(Calendar.HOUR_OF_DAY, hour);
        cld.set(Calendar.MINUTE, min);
        cld.set(Calendar.SECOND, sec);
        return cld.getTime().getTime();
    }


//end by add

    /**
     *  将字符串转换为Oracle需要的日期字符串 add by wjz 030924
     *
     *@param  date
     *@return
     */
    public static String toOracleDateFormat(String date) {
//comment by wjz 0211
//        if (date == null) {
//            return null;
//        }
//        if (date.length() == 10) {
//            return "to_date('" + date + "','YYYY-MM-DD')";
//        } else if (date.length() == 8) {
//            return "to_date('" + date + "','YYYYMMDD')";
//        } else {
//            return null;
//        }
//end comment
        //add by wjz 0211
        return getOracleDateFormat(date);
        //end add
    }
    
    
    /**
     * 获取不同的java.sql.Date日期时间格式
     * @param date
     * @return
     */
    public static java.sql.Date toSqlDate(Date date) {
    	return new java.sql.Date(date.getTime());
    }
    
    public static java.sql.Time toSqlTime(Date date) {
    	return new java.sql.Time(date.getTime());
    }
    
    public static java.sql.Timestamp toSqlTimestamp(Date date) {
    	return new java.sql.Timestamp(date.getTime());
    }
    
    public static java.sql.Timestamp toSqlTimestamp(String date) {
    	return new java.sql.Timestamp(strToDate(date).getTime());
    }
    
    public static String formatDate(String date, String format) {
    	return dateToStr(strToDate(date), format);
    }
    
    public static boolean gpsTimeValid(String gpstimestr, int maxdiffdate) {
    	Date gpstime = null;
    	try {
    		gpstime = strToDate(gpstimestr);
		} catch (Exception e) {
			return false;
		}
		Date nowdate = new Date();
		int diff = diffDate(nowdate, gpstime);
		if (diff<0||diff>=maxdiffdate)return false;
		return true;
    }
    
    public static boolean gpsTimeValid(String gpstimestr) {
    	return gpsTimeValid(gpstimestr, 30);
    }

	/**
	 * 
	 * getDateAddHours:(获取指定时间之后多少小时的时间). 
	 *
	 * @author sid
	 * @param date
	 * @param hours
	 * @return
	 */
	public static Date getDateAddHours(Date date,int hours){
		return DateUtil.getDateAddMinutes(date, 60*hours);
	}
	/**
	 * 
	 * getDateAddMinutes:(获取指定时间之后多少分钟的时间). 
	 *
	 * @author sid
	 * @param date
	 * @param minutes
	 * @return
	 */
	public static Date getDateAddMinutes(Date date,int minutes){
		
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		cl.add(Calendar.MINUTE	, minutes);
		
		return cl.getTime();
	}
}

