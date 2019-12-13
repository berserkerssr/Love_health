package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author TALON WAT
 * @date 2019-12-13 8:37
 */
@RestController
@RequestMapping(value = "/role")
public class RoleController {
    @Reference
    private RoleService roleService;

    // 分页查询检查项列表
    @RequestMapping(value = "/findPage")
    //@PreAuthorize(value = "hasAuthority('ROLE_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = roleService.findPage(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    // ID查询检查项（回显）
    @RequestMapping(value = "/findById")
    public Result findById(Integer id){
        Role role = roleService.findById(id);
        if(role!=null){
            return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS,role);
        }
        else{
            return new Result(false,MessageConstant.QUERY_ROLE_FAIL);
        }
    }

    // 使用角色ID，查询的权限ID集合
    @RequestMapping(value = "/findPermissionIdsByRoleId")
    public List<Integer> findPermissionIdsByRoleId(Integer id){
        List<Integer> list  = roleService.findPermissionIdsByRoleId(id);
        return list;
    }

    // 使用角色ID，查询的菜单ID集合
    @RequestMapping(value = "/findMenuIdsByRoleId")
    public List<Integer> findMenuIdsByRoleId(Integer id){
        List<Integer> list  = roleService.findMenuIdsByRoleId(id);
        return list;
    }

    // 新增角色
    @RequestMapping(value = "/add")
    public Result add(@RequestBody Role role, Integer [] permissionIds, Integer [] menuIds){
        try {
            roleService.add(role,permissionIds,menuIds);
            return new Result(true, MessageConstant.ADD_ROLE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_ROLE_FAIL);
        }
    }

    // 编辑角色
    @RequestMapping(value = "/edit")
    public Result edit(@RequestBody Role role, Integer [] permissionIds, Integer [] menuIds){
        try {
            roleService.edit(role,permissionIds,menuIds);
            return new Result(true, MessageConstant.EDIT_ROLE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_ROLE_FAIL);
        }
    }

    // 删除角色
    @RequestMapping(value = "/deleteById")
    //@PreAuthorize(value = "hasAuthority('CHECKITEM_DELETE_ABC')")
    public Result deleteById(Integer id){
        try {
            roleService.deleteById(id);
            return new Result(true, MessageConstant.DELETE_ROLE_SUCCESS);
        } catch(RuntimeException e){
            return new Result(false,e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_ROLE_FAIL);
        }
    }

}
