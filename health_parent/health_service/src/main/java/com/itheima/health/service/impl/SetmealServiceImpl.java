package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.Date;
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
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealDao setmealDao;

    @Autowired
    CheckGroupDao checkGroupDao;

    @Autowired
    CheckItemDao checkItemDao;

    @Autowired
    JedisPool jedisPool;


    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //1：保存套餐，返回套餐ID，封装到setmeal中的id属性
        setmealDao.add(setmeal);
        //2：遍历循环，关联中间表
        if(checkgroupIds!=null && checkgroupIds.length>0){
            setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
        }
        // 在保存套餐数据的时候，同时向redis中存放数据（目的：用于清除七牛云上的垃圾图片），使用redis的集合方式存放
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCE,setmeal.getImg());
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setmealDao.findByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    // 查询所有套餐
    @Override
    public List<Setmeal> findAll() {
        String RedisSetMealList = jedisPool.getResource().get("RedisSetMealList");
        if (RedisSetMealList == null) {
            List<Setmeal> list = setmealDao.findAll();
            System.out.println("走数据库查询/n"+list);
            String JSONSetMealList = JSON.toJSON(list).toString();
            jedisPool.getResource().set("RedisSetMealList",JSONSetMealList);
            return list;
        }
        List<Setmeal> setMeals = JSON.parseArray(RedisSetMealList,Setmeal.class);
        System.out.println("走redis查询/n"+setMeals);
        return setMeals;
    }

    // 使用mybatis的映射
    // 使用套餐id，查询套餐详情
    @Override
    public Setmeal findById(Integer id) {
        // 1：使用套餐id，查询套餐对象
        String RedisSetMeal = jedisPool.getResource().get("RedisSetMeal"+id);
        if (RedisSetMeal == null) {
            Setmeal setmeal = setmealDao.findById(id);
            System.out.println("走数据库查询/n" + setmeal);
            String JSONSetMeal = JSON.toJSON(setmeal).toString();
            jedisPool.getResource().set("RedisSetMeal"+id,JSONSetMeal);
            return setmeal;
        }
        Setmeal setMeal = JSON.parseObject(RedisSetMeal,Setmeal.class);
        System.out.println("走redis查询/n" + setMeal);
        return setMeal;
    }

    @Override
    public List<Map> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    // 使用检查组ID，查询检查项的ID集合
    @Override
    public List<Integer> findCheckGroupsBySetMealId(Integer id) {
        return setmealDao.findCheckGroupsBySetMealId(id);
    }

    //编辑套餐
    @Override
    public void edit(Setmeal setmeal, Integer... checkgroupIds) {
        // 一：根据套餐编辑页面填写情况，更新对应套餐数据，更新t_setmeal
        setmealDao.edit(setmeal);
        // 二：操作中间表数据t_setmeal_checkgroup
        // 1：使用套餐的id，先删除中间表数据
        setmealDao.deleteSetMealAndCheckGroupBySetMealId(setmeal.getId());
        // 2：使用套餐id和传递的检查组id的数组，向中间表中添加数据
        this.setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
    }


    // 遍历循环，向套餐和检查组的中间表关联数据
    private void setSetmealAndCheckGroup(Integer setmealId, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            Map map = new HashMap();
            map.put("setmealId",setmealId);
            map.put("checkgroupId",checkgroupId);
            setmealDao.addSetmealAndCheckGroup(map);
        }

    }

    //删除套餐
    @Override
    public void delete(Integer id) throws Exception {
        long count = 0;
        count = setmealDao.findCountBySetMealId(id);
        if ( count > 0 ) {
            throw new RuntimeException("请先清除关联的检查组再执行删除操作");
        }
        List<Order> orders = setmealDao.findOrderBySetMealId(id);
        String today = DateUtils.parseDate2String(new Date());
        if(orders != null || orders.size() > 0){
            for (Order order : orders) {
                if (today.compareTo(DateUtils.parseDate2String(order.getOrderDate())) <= 0) {
                    if(order.getOrderStatus().equals("已到诊")) {
                        setmealDao.deleteOrder(order.getId());
                    }else{
                        throw new RuntimeException("当前套餐有未到珍的预约");
                    }
                }else if(today.compareTo(DateUtils.parseDate2String(order.getOrderDate())) > 0){
                    setmealDao.deleteOrder(order.getId());
                }
            }
        }

        /** 删除img缓存 */
        Setmeal setmeal = setmealDao.findById(id);
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCE,setmeal.getImg());

        setmealDao.delete(id);

    }
}
