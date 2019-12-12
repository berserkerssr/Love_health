package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService{

    @Autowired
    OrderSettingDao orderSettingDao;

    @Override
    public void addList(List<OrderSetting> orderSettingList) {
        if(orderSettingList!=null && orderSettingList.size()>0){
            for (OrderSetting orderSetting : orderSettingList) {
                // 判断，当前预约时间是否在数据库中存在记录
                long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if(count==0){
                    // 如果不存在记录，新增
                    // 新增
                    orderSettingDao.add(orderSetting);
                }
                // 如果存在记录，使用预约时间更新最多预约人数
                else{
                    orderSettingDao.updateNumberByOrderDate(orderSetting);
                }

            }
        }
    }

    @Override
    public List<Map> findOrderSettingByOrderDate(String date) {
        // date的格式2019-12
        String begin = date+"-01";
        String end = date+"-31";
        Map<String,Object> params = new HashMap<>();
        params.put("begin",begin);
        params.put("end",end);
        // 完成查询
        List<OrderSetting> list = orderSettingDao.findOrderSettingByOrderDate(params);
        // 组织查询结果的数据List<Map>
        List<Map> mapList = new ArrayList<>();
        if(list!=null && list.size()>0){
            for (OrderSetting orderSetting : list) {
                Map map = new HashMap();
                map.put("date",orderSetting.getOrderDate().getDate());
                map.put("number",orderSetting.getNumber());
                map.put("reservations",orderSetting.getReservations());
                mapList.add(map);
            }
        }
        return mapList;
    }

    @Override
    public void updateNumberByOrderDate(OrderSetting orderSetting) {
        // 判断，当前预约时间是否在数据库中存在记录
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if(count==0){
            // 如果不存在记录，新增
            // 新增
            orderSettingDao.add(orderSetting);
        }
        // 如果存在记录，使用预约时间更新最多预约人数
        else{
            orderSettingDao.updateNumberByOrderDate(orderSetting);
        }
    }
    @Override
    public void deleteOrderSettingByOrderDate(String today) {
        orderSettingDao.deleteOrderSettingByOrderDate(today);
    }
}
