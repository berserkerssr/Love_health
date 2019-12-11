package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
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
@RequestMapping(value = "/login")
public class LoginMobileController {

    @Reference
    MemberService memberService;

    @Autowired
    JedisPool jedisPool;

    // 手机快速登录
    @RequestMapping(value = "/check")
    public Result check(@RequestBody Map map, HttpServletResponse response){
        try {
            // * 获取手机号和验证码（页面）
            String telephone = (String)map.get("telephone");
            String validateCode = (String)map.get("validateCode");
            // * 通过手机号从Redis中获取验证码
            String redisValidateCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
            //* 对手机号输入验证码做校验，页面输入的验证码和Redis中存放的验证码是否一致
            if(redisValidateCode == null || !redisValidateCode.equals(validateCode)){
                // * 如果不一致，提示【验证码输入有误】
                return new Result(false,MessageConstant.VALIDATECODE_ERROR);
            }
            // 如果校验成功
            // 判断当前用户是否是会员，不是会员，自动注册会员
            Member member = memberService.findMemberByTelephone(telephone);
            if(member == null){
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date()); // 注册时间当前时间
                memberService.add(member);
            }
            // 保存用户的登录状态
            Cookie cookie = new Cookie("member_login_telephone_80",telephone);
            cookie.setPath("/"); // 有效路径
            cookie.setMaxAge(30*24*60*60);  // 有效时间（单位：秒）
            response.addCookie(cookie);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.LOGIN_FAIL);
        }
    }


}
