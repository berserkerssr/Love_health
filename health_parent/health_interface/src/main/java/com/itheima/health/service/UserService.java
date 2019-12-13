package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    User findUserByUsername(String username);

    List<Map<String, Object>> getMenuList(String username);

    List<Menu> getMenuListDemo02(String username);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);


}
