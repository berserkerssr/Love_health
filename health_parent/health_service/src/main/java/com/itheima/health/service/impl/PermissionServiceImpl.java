package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.PermissionDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author TALON WAT
 * @date 2019-12-12 16:31
 */
@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;

    /**
     * 分页
     */
    @Override
    public PageResult findAll(Integer currentPage, Integer pageSize, String queryString) {
        Page<CheckItem> page = PageHelper.startPage(currentPage, pageSize);
        List<CheckItem> list = permissionDao.findPage(queryString);
        return new PageResult(page.getTotal(),list);
    }

    @Override
    public void add(Permission permission) {
        permissionDao.add(permission);
    }
}
