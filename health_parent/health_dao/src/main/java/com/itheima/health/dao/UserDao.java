package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.User;

import java.util.List;

public interface UserDao {

    User findUserByUsername(String username);

    List<Menu> findMenuListByUserId(Integer id);

    List<Menu> findMenuListLevel2ByMenuId(Integer id);

    Page<CheckItem> findByCondition(String queryString);
}
