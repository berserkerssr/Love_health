package com.itheima.health.dao;

import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionDao {

    Set<Permission> findPermissionsByRoleId(Integer roleId);

    List<CheckItem> findPage(String queryString);

    void add(Permission permission);

    Permission findById(Integer id);

    void edit(Permission permission);

    void deleteById(Integer id);

    List<Permission> findAlls();
}
