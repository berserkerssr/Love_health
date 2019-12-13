package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Role;

import java.util.List;

/**
 * @author TALON WAT
 * @date 2019-12-13 8:38
 */
public interface RoleService {
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    Role findById(Integer id);

    List<Integer> findPermissionIdsByRoleId(Integer id);

    List<Integer> findMenuIdsByRoleId(Integer id);

    void add(Role role, Integer[] permissionIds, Integer[] menuIds);

    void edit(Role role, Integer[] permissionIds, Integer[] menuIds);

    void deleteById(Integer id);
}
