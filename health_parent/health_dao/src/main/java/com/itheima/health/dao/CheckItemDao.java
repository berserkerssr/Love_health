package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    void add(CheckItem checkItem);

    // List<CheckItem> findByCondition(String queryString);
    Page<CheckItem> findByCondition(String queryString);

    void deleteById(Integer id);

    long findCheckGroupAndCheckItemCountByCheckItemId(Integer id);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();

    List<CheckItem> findCheckItemsByCheckGroupId(Integer id);
}
