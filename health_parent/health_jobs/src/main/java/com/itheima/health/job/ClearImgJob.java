package com.itheima.health.job;

import com.itheima.health.constant.RedisConstant;
import com.itheima.health.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

/**
 * @ClassName ClearImgJob
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/12/1 12:21
 * @Version V1.0
 */
public class ClearImgJob {

    @Autowired
    JedisPool jedisPool;

    // 删除图片的方法
    public void deletePic(){
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCE, RedisConstant.SETMEAL_PIC_DB_RESOURCE);
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()){
            String pic = iterator.next();
            System.out.println("删除的图片："+pic);
            // 删除七牛云上的图片
            QiniuUtils.deleteFileFromQiniu(pic);
            // 删除Redis中key值为SETMEAL_PIC_RESOURCE的数据
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCE,pic);
        }
    }
}
