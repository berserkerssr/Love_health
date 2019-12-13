package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;
import org.springframework.stereotype.Repository;


public interface MenuDao {
    Page<CheckItem> findByCondition(String queryString);
}
