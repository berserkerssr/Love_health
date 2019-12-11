package com.itheima.test;

import com.itheima.health.utils.ValidateCodeUtils;
import org.junit.Test;

/**
 * @ClassName TestValidateCode
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/12/4 9:08
 * @Version V1.0
 */
public class TestValidateCode {

    @Test
    public void testCode(){
        // 4位验证码
        Integer code4 = ValidateCodeUtils.generateValidateCode(4);
        System.out.println(code4);
        Integer code6 = ValidateCodeUtils.generateValidateCode(6);
        System.out.println(code6);
    }
}
