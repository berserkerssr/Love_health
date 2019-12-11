package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {

    void add(CheckGroup checkGroup);

    //void addCheckGroupAndCheckItem(@Param(value = "checkgroupId") Integer checkgroupId, @Param(value = "checkitemId") Integer checkitemId);

    void addCheckGroupAndCheckItem(Map<String, Object> map);

    Page<CheckGroup> findByCondition(String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup);

    void deleteCheckGroupAndCheckItemByCheckGroupId(Integer id);

    List<CheckGroup> findAll();

    List<CheckGroup> findCheckGroupsBySetmealId(Integer id);
}
