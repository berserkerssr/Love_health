package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface MenuDao {
    Page<CheckItem> findByCondition(String queryString);

    void add(Menu menu);

    List<Menu> findAll();
}
