package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.service.ReportService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CheckItemServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/28 10:03
 * @Version V1.0
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    MemberDao memberDao;

    @Autowired
    OrderDao orderDao;


    @Override
    public Map findBusinessReportData() throws Exception {
        Map map = new HashMap();
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
        /***********************会员相关统计***********************************/
        // 今天新增会员数
        Integer todayNewMember = memberDao.findTodayNewMember(reportDate);
        // 总会员数
        Integer totalMember = memberDao.findTotalMember();
        // 本周新增会员数
        Integer thisWeekNewMember = memberDao.findThisNewMember(thisWeekMonday);
        // 本月新增会员数
        Integer thisMonthNewMember = memberDao.findThisNewMember(thisMonthFrist);

        /************************预约订单相关统计*********************************/
        // 今天预约数
        Integer todayOrderNumber = orderDao.findTodayOrderNumber(reportDate);
        // 今天到诊数
        Integer todayVisitsNumber = orderDao.findTodayVisitsNumber(reportDate);
        // 本周预约数
        Map<String,Object> weekMap = new HashMap<>();
        weekMap.put("begin",thisWeekMonday);
        weekMap.put("end",thisWeekSunday);
        Integer thisWeekOrderNumber = orderDao.findThisOrderNumber(weekMap);
        // 本周到诊数
        Map<String,Object> weekMap2 = new HashMap<>();
        weekMap2.put("begin",thisWeekMonday);
        weekMap2.put("end",thisWeekSunday);
        Integer thisWeekVisitsNumber = orderDao.findThisVisitsNumber(weekMap2);
        // 本月预约数
        Map<String,Object> monthMap = new HashMap<>();
        monthMap.put("begin",thisMonthFrist);
        monthMap.put("end",thisMonthLast);
        Integer thisMonthOrderNumber = orderDao.findThisOrderNumber(monthMap);
        // 本月到诊数
        Map<String,Object> monthMap2 = new HashMap<>();
        monthMap2.put("begin",thisMonthFrist);
        monthMap2.put("end",thisMonthLast);
        Integer thisMonthVisitsNumber = orderDao.findThisVisitsNumber(monthMap2);

        /*****************************热门套餐*********************************************/

        List<Map> hotSetmeal = orderDao.findHotSetmeal();

        map.put("reportDate",reportDate); // String

        map.put("todayNewMember",todayNewMember); // Integer
        map.put("totalMember",totalMember); // Integer
        map.put("thisWeekNewMember",thisWeekNewMember); // Integer
        map.put("thisMonthNewMember",thisMonthNewMember); // Integer

        map.put("todayOrderNumber",todayOrderNumber); // Integer
        map.put("todayVisitsNumber",todayVisitsNumber); // Integer
        map.put("thisWeekOrderNumber",thisWeekOrderNumber); // Integer
        map.put("thisWeekVisitsNumber",thisWeekVisitsNumber); // Integer
        map.put("thisMonthOrderNumber",thisMonthOrderNumber); // Integer
        map.put("thisMonthVisitsNumber",thisMonthVisitsNumber); // Integer

        map.put("hotSetmeal",hotSetmeal); // List<Map>
        return map;
    }
}
