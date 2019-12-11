package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService{

    @Autowired
    OrderDao orderDao;

    @Autowired
    MemberDao memberDao;

    @Autowired
    OrderSettingDao orderSettingDao;

    @Override
    public Result submitOrder(Map map) {
        try {
            // 获取预约日期
            String orderDate = (String)map.get("orderDate");
            // 将字符串转换成日期
            Date date = null; // 预约时间
            date = DateUtils.parseString2Date(orderDate);

            // 1：使用预约体检日期作为条件，查询预约设置表，判断当前时间是否可以进行预约
            OrderSetting orderSetting = orderSettingDao.getOrderSettingByOrderDate(date);
            // * 如果查询结果为空，表示预约设置表中没有设置体检日期指定的时间，提示【当前时间不能预约】
            if(orderSetting == null){
                return new Result(false,MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            }
            // 2：获取预约设置表中的number（最多预约人数）、reservations（实际预约人数）
            int number = orderSetting.getNumber();
            int reservations = orderSetting.getReservations();
            // * 如果reservations>=number，表示预约已满，提示【预约已满】
            if(reservations>=number){
                return new Result(false,MessageConstant.ORDER_FULL);
            }
            // 3：使用输入的手机号查询会员表，判断当前手机是否是会员
            // 获取手机号
            String telephone = (String) map.get("telephone");
            Member member = memberDao.findMemberByTelephone(telephone);
            //* 如果是会员
            //* 组织查询条件（会员id、预约时间、套餐id）对订单表（t_order）进行查询
            //* 如果存在数据，提示【当前时间已经预约，不能重复预约】
            if(member!=null){
                // 组织查询条件（实体、Map）
                Order order = new Order(member.getId(),date,null,null,Integer.parseInt((String)map.get("setmealId")));
                List<Order> list = orderDao.findOrderListByCondition(order);
                if(list!=null && list.size()>0){
                    return new Result(false,MessageConstant.HAS_ORDERED);
                }
            }
            //* 如果不是会员
            //* 自动注册会员，向t_member表中插入数据
            else{
                member = new Member();
                member.setPhoneNumber((String)map.get("telephone")); // 手机号
                member.setName((String)map.get("name"));  // 姓名
                member.setSex((String)map.get("sex"));   // 性别
                member.setIdCard((String)map.get("idCard")); // 身份证
                member.setRegTime(new Date());             // 注册时间（当前时间）
                memberDao.add(member); // 保存后，返回会员id
            }
            // 4：可以预约，更新t_ordersetting表的reservations字段加1，表示预约1人
            orderSettingDao.updateReservatioinsByOrderDate(date);
            //5：可以预约，向t_order表中插入一条数据
            Order order = new Order(member.getId(),date,(String)map.get("orderType"),Order.ORDERSTATUS_NO,Integer.parseInt((String)map.get("setmealId")));
            orderDao.add(order);
            return new Result(true, MessageConstant.ORDER_SUCCESS,order);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抛出运行时异常！");
            //return new Result(false, MessageConstant.ORDER_ERROR);
        }
    }

    @Override
    public Map<String, Object> findById(Integer id) {
        Map<String,Object> map = orderDao.findById(id);
        if(map!=null){
            Date date = (Date) map.get("orderDate");
            // 将日期类型转成字符串类型
            try {
                String sDate = DateUtils.parseDate2String(date);
                map.put("orderDate",sDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
