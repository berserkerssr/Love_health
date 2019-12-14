package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {

    void add(Setmeal setmeal);

    void addSetmealAndCheckGroup(Map map);

    Page<Setmeal> findByCondition(String queryString);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    List<Map> findSetmealCount();

    List<Integer> findCheckGroupsBySetMealId(Integer id);

    void edit(Setmeal setmeal);

    void deleteSetMealAndCheckGroupBySetMealId(Integer id);

    long findCountBySetMeal(Integer id);

    void delete(Integer id);

    long findCountBySetMealId(Integer id);

    List<Order> findOrderBySetMealId(Integer id);

    void deleteOrder(Integer id);
}
