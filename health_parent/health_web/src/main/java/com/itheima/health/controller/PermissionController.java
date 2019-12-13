package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author TALON WAT
 * @date 2019-12-12 16:31
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Reference
    private PermissionService permissionService;

    @RequestMapping("/findPage")
    @PreAuthorize(value = "hasAuthority('PERMISSION_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return permissionService.findAll(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
    }

    // 新增 权限项
    @RequestMapping(value = "/add")
    @PreAuthorize(value = "hasAuthority('PERMISSION_ADD')")
    public Result add(@RequestBody Permission permission){
        try {
            permissionService.add(permission);
            return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_CHECKITEM_FAIL);
        }
    }
    // ID查询检查项（回显）
    @RequestMapping(value = "/findById")
    public Result findById(Integer id){
        Permission permission = permissionService.findById(id);
        if(permission!=null){
            return new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,permission);
        }
        else{
            return new Result(false,MessageConstant.QUERY_PERMISSION_FAIL);
        }
    }
    @RequestMapping(value = "/edit")
    @PreAuthorize(value = "hasAuthority('PERMISSION_EDIT')")
    public Result edit(@RequestBody Permission permission){
        try {
            permissionService.edit(permission);
            return new Result(true, MessageConstant.QUERY_PERMISSION_EDIT_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_PERMISSION_EDIT_FAIL);
        }
    }
    // 删除检查项
    @RequestMapping(value = "/delete")
    @PreAuthorize(value = "hasAuthority('PERMISSION_DELETE')")
    public Result deleteById(Integer id){
        try {
            permissionService.deleteById(id);
            return new Result(true, MessageConstant.DELETE_PERMISSION_SUCCESS);
        }  catch(Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_PERMISSION_FAIL);
        }
    }
    // 查询所有检查项
    @RequestMapping(value = "/findAll")
    public Result findAll(){
        List<Permission> list = permissionService.findAlls();
        if(list!=null && list.size()>0){
            return new Result(true,MessageConstant.QUERY_PERMISSION_LIST_SUCCESS,list);
        }
        else{
            return new Result(false,MessageConstant.QUERY_PERMISSION_LIST_FAIL);
        }
    }

}
