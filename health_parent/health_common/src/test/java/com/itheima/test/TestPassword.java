package com.itheima.test;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @ClassName TestPassword
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/12/7 12:12
 * @Version V1.0
 */
public class TestPassword {

    @Test
    public void testSpringSecurityPassword(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        // 保存，保存到数据库，是一个加密的结果
        String s1 = bCryptPasswordEncoder.encode("123"); // $2a$10$duwpdwhZFXGlFJoCrzOjpucfBglVLMKmnfnqmlzP3VtBGCEhXl816
        String s2 = bCryptPasswordEncoder.encode("123"); // $2a$10$1DbunIPJ0u8zG53olL24w.xws7X4aqci4YfgeT3x3cX.29DvyhGVi
        System.out.println(s1);
        System.out.println(s2);

        boolean flag = bCryptPasswordEncoder.matches("admin", "$2a$10$1DbunIPJ0u8zG53olL24w.xws7X4aqci4YfgeT3x3cX.29DvyhGVi");
        System.out.println(flag);
    }
}
