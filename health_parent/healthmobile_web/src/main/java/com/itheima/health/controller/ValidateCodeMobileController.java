package com.itheima.health.controller;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @ClassName CheckItemController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/28 10:04
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/validateCode")
public class ValidateCodeMobileController {


    @Autowired
    JedisPool jedisPool;

    // 发送验证码（体检预约场景）
    @RequestMapping(value = "/send4Order")
    public Result send4Order(String telephone){
        try {
            // 1：获取手机号
            // 2：生成4位验证码
            Integer code4 = ValidateCodeUtils.generateValidateCode(4);
            // 3：使用SMSUtils工具类，根据手机号发送短信
            // SMSUtils.sendShortMessage(telephone,code4.toString());
            System.out.println("发送的短信是："+code4);
            /**4：将手机号和验证码存放到Redis中（手机号唯一）
            key                                value
            手机号+001                生成验证码
             */
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,5*60,code4.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    // 发送验证码（体检预约场景）
    @RequestMapping(value = "/send4Login")
    public Result send4Login(String telephone){
        try {
            // 1：获取手机号
            // 2：生成4位验证码
            Integer code4 = ValidateCodeUtils.generateValidateCode(4);
            // 3：使用SMSUtils工具类，根据手机号发送短信
            // SMSUtils.sendShortMessage(telephone,code4.toString());
            System.out.println("发送的短信是："+code4);
            /**4：将手机号和验证码存放到Redis中（手机号唯一）
             key                                value
             手机号+001                生成验证码
             */
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,5*60,code4.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

}
