package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

/**
 * @ClassName CheckItemController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/28 10:04
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/setmeal")
public class SetmealController {

    @Reference
    SetmealService setmealService;

    @Autowired
    JedisPool jedisPool;

    // 图片上传，上传到七牛云
    @RequestMapping(value = "/upload")
    public Result upload(@RequestParam(value = "imgFile") MultipartFile imgFile){
        try {
            // 如何获取文件名(1.jpg)
            String filename = imgFile.getOriginalFilename();
            // 使用UUID的方式生成文件名
            filename = UUID.randomUUID().toString()+filename.substring(filename.lastIndexOf("."));
            // 上传图片
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),filename);
            // 向七牛云保存图片的同时，再向Redis中存放数据（目的：用于删除七牛云上的垃圾图片），采用Redis中的集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCE,filename);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,filename);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    // 新增套餐
    @RequestMapping(value = "/add")
    @PreAuthorize(value = "hasAuthority('SETMEAL_ADD')")
    public Result add(@RequestBody Setmeal setmeal, Integer [] checkgroupIds){
        try {
            setmealService.add(setmeal,checkgroupIds);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    // 分页查询套餐列表
    @RequestMapping(value = "/findPage")
    @PreAuthorize(value = "hasAuthority('SETMEAL_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = setmealService.findPage(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    // 1：使用套餐id，回显套餐的基本信息
    @RequestMapping(value = "/findById")
    public Result findById(Integer id){
        Setmeal setmeal = setmealService.findById(id);
        if(setmeal!=null){
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,setmeal);
        }
        else{
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    // 使用套餐ID，查询检查组的ID集合
    @RequestMapping(value = "/findCheckGroupsBySetMealId")
    public List<Integer> findCheckGroupsBySetMealId(Integer id){
        List<Integer> list  = setmealService.findCheckGroupsBySetMealId(id);
        return list;
    }

    // 编辑套餐
    @RequestMapping(value = "/edit")
    @PreAuthorize(value = "hasAuthority('SETMEAL_EDIT')")
    public Result edit(@RequestBody Setmeal setmeal, Integer [] checkgroupIds){
        try {
            setmealService.edit(setmeal,checkgroupIds);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    //删除套餐
    @RequestMapping("/delete")
    @PreAuthorize(value = "hasAuthority('SETMEAL_DELETE')")
    public Result delete(Integer id){
        try {
            setmealService.delete(id);
        } catch (RuntimeException e) {
            return new Result(false,e.getMessage());
        } catch (Exception e){
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

}
