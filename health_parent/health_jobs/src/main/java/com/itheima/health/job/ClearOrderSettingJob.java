package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.DateUtils;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class ClearOrderSettingJob {

    @Reference
    private OrderSettingService orderSettingService;

    public void deleteOrderSetting() throws Exception {
        //调用业务层的方法执行删除历史预约
        String today = DateUtils.parseDate2String(new Date());
        orderSettingService.deleteOrderSettingByOrderDate(today);
    }
}
