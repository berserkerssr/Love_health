package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Role;

import java.util.List;
import java.util.Map;
import java.util.List;
import java.util.Set;

public interface RoleDao {

    Set<Role> findRolesByUserId(Integer userId);

    Page<CheckItem> findByCondition(String queryString);

    Role findById(Integer id);

    List<Integer> findPermissionIdsByRoleId(Integer id);

    List<Integer> findMenuIdsByRoleId(Integer id);

    void add(Role role);

    void setRoleAndPermission(Map<String, Object> map);

    void setRoleAndMenu(Map<String, Object> map);

    void update(Role role);

    void removeRoleAndPermission(Integer id);

    void removeRoleAndMenu(Integer id);

    long findRoleAndUserCountByRoleId(Integer id);

    void deleteById(Integer id);

    List<Role> findAll();
}
