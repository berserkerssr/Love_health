package com.itheima.test;

import com.itheima.health.utils.DateUtils;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName TestDate
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/12/8 11:54
 * @Version V1.0
 */
public class TestDate {

    @Test
    public void test(){
        // 1：数据集合，存放months
        List<String> months = new ArrayList<>();
        // 统计过去1年的时间，按月统计（格式：年-月，2019-12）
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MONTH,-12);
        for (int i = 0; i < 12; i++) {
            instance.add(Calendar.MONTH,1);
            Date time = instance.getTime(); // 获取当前时间（2019-01，2019-02， 2019-03）
            String date = new SimpleDateFormat("yyyy-MM").format(time);
            months.add(date);
        }
        System.out.println(months);
    }

    @Test
    public void testReport() throws Exception {
        // 当前时间（今天）
        String reportDate = DateUtils.parseDate2String(DateUtils.getToday());
        // 根据当前时间计算本周的周一
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        // 根据当前时间计算本周的周日
        String thisWeekSunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
        // 根据当前时间计算本月的第1天
        String thisMonthFrist = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        // 根据当前时间计算本月的最后1天
        String thisMonthLast = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());
    }
}
