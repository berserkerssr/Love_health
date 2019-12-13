package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TALON WAT
 * @date 2019-12-13 8:38
 */
@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        // 1：初始化分页的参数
        PageHelper.startPage(currentPage,pageSize);
        // 2：查询
        Page<CheckItem> page = roleDao.findByCondition(queryString);
        // 3：封装PageResult数据
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public Role findById(Integer id) {
        return roleDao.findById(id);
    }

    @Override
    public List<Integer> findPermissionIdsByRoleId(Integer id) {
        return roleDao.findPermissionIdsByRoleId(id);
    }

    @Override
    public List<Integer> findMenuIdsByRoleId(Integer id) {
        return roleDao.findMenuIdsByRoleId(id);
    }

    @Override
    public void add(Role role, Integer[] permissionIds, Integer[] menuIds) {
        //增加role
        roleDao.add(role);

        //增加 role permission 关联
        this.setRoleAndPermission(role.getId(),permissionIds);

        //增加 role menu 关联
        this.setRoleAndMenu(role.getId(),menuIds);
    }

    @Override
    public void edit(Role role, Integer[] permissionIds, Integer[] menuIds) {
        //更新 role
        roleDao.update(role);

        //删除 原来 role permission 关联
        roleDao.removeRoleAndPermission(role.getId());

        //增加 新 role permission 关联
        this.setRoleAndPermission(role.getId(),permissionIds);

        //删除 原来 role menu 关联
        roleDao.removeRoleAndMenu(role.getId());

        //增加 新 role menu 关联
        this.setRoleAndMenu(role.getId(),menuIds);
    }

    @Override
    public void deleteById(Integer id) {
        //查看 在 用户表中是否有关联
        long count = roleDao.findRoleAndUserCountByRoleId(id);
        if (count > 0){
            throw new RuntimeException("当前角色在用户表中存在数据不能删除!");
        }
        //删除角色
        //  删除关联
        roleDao.removeRoleAndMenu(id);
        roleDao.removeRoleAndPermission(id);
        //  删除 role
        roleDao.deleteById(id);
    }

    //增加 role permission 关联
    private void setRoleAndPermission(Integer roleId, Integer[] permissionIds) {
        if(permissionIds!=null && permissionIds.length>0){
            for (Integer permissionId : permissionIds) {
                Map<String,Object> map = new HashMap<>();
                map.put("roleId",roleId);
                map.put("permissionId",permissionId);
                roleDao.setRoleAndPermission(map);
            }
        }
    }

    //增加 role menu 关联
    private void setRoleAndMenu(Integer roleId, Integer[] menuIds) {
        if(menuIds!=null && menuIds.length>0){
            for (Integer menuId : menuIds) {
                Map<String,Object> map = new HashMap<>();
                map.put("roleId",roleId);
                map.put("menuId",menuId);
                roleDao.setRoleAndMenu(map);
            }
        }
    }
}
