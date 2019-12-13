package com.itheima.health.service;

import com.itheima.health.entity.PageResult;

/**
 * @author TALON WAT
 * @date 2019-12-13 8:20
 */
public interface MenuService {
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);
}
