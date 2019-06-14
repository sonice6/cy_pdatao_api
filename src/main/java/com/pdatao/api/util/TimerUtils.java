package com.pdatao.api.util;

import java.util.Date;

public class TimerUtils {
	
	/***
	 * 得到两个日期之间相差多少天
	 * @param date1 得到得日期
	 * @param date2 当前日期
	 * @return
	 */
	public static int differentDaysByMillisecond(Date date1,Date date2){
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }

}
