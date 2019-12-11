package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @ClassName CheckItemController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/28 10:04
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/order")
public class OrderMobileController {

    @Reference
    OrderService orderService;

    @Autowired
    JedisPool jedisPool;

    // 体检预约
    @RequestMapping(value = "/submit")
    public Result submit(@RequestBody Map map){
        try {
            // * 获取手机号和验证码（页面）
            String telephone = (String)map.get("telephone");
            String validateCode = (String)map.get("validateCode");
            // * 通过手机号从Redis中获取验证码
            String redisValidateCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
            //* 对手机号输入验证码做校验，页面输入的验证码和Redis中存放的验证码是否一致
            if(redisValidateCode == null || !redisValidateCode.equals(validateCode)){
                // * 如果不一致，提示【验证码输入有误】
                return new Result(false,MessageConstant.VALIDATECODE_ERROR);
            }
            //* 传递参数OrderInfo，设置map的值orderType，表示微信预约，给Service处理（Service进行校验）
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            Result result;
            result = orderService.submitOrder(map);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDER_ERROR);
        }
    }


    // 使用订单id，查询订单的详情
    @RequestMapping(value = "/findById")
    public Result findById(Integer id){
        try {
            Map<String,Object> map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
