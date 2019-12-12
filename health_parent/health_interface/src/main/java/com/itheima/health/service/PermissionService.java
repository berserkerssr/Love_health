package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Permission;

/**
 * @author TALON WAT
 * @date 2019-12-12 16:31
 */
public interface PermissionService {
    PageResult findAll(Integer currentPage, Integer pageSize, String queryString);

    void add(Permission permission);
}
