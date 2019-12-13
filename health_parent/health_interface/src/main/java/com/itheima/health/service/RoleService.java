package com.itheima.health.service;

import com.itheima.health.entity.PageResult;

/**
 * @author TALON WAT
 * @date 2019-12-13 8:38
 */
public interface RoleService {
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);
}
