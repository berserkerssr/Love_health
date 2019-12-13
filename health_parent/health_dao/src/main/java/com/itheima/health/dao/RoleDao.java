package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Role;

import java.util.Set;

public interface RoleDao {

    Set<Role> findRolesByUserId(Integer userId);

    Page<CheckItem> findByCondition(String queryString);
}
