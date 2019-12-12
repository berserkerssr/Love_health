package com.itheima.health.service;

import com.itheima.health.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    User findUserByUsername(String username);

    List<Map<String, Object>> getMenuList(String username);
}
