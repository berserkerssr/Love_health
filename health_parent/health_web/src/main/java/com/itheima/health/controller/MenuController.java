package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author TALON WAT
 * @date 2019-12-13 8:20
 */
@RestController
@RequestMapping(value = "/menu")
public class MenuController {

    @Reference
    private MenuService menuService;

    // 分页查询检查项列表
    @RequestMapping(value = "/findPage")
    //@PreAuthorize(value = "hasAuthority('MENU_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = menuService.findPage(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }
    @RequestMapping(value = "/add")
    //@PreAuthorize(value = "hasAuthority('CHECKITEM_ADD')")
    public Result add(@RequestBody Menu menu){
        try {
            menuService.add(menu);
            return new Result(true, MessageConstant.ADD_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_MENU_FAIL);
        }
    }
    // ID查询检查项（回显）
    @RequestMapping(value = "/findById")
    public Result findById(Integer id){
        Menu menu = menuService.findById(id);
        if(menu!=null){
            return new Result(true,MessageConstant.QUERY_MENU_SUCCESS,menu);
        }
        else{
            return new Result(false,MessageConstant.QUERY_MENU_FAIL);
        }
    }
    // 查询所有检查项
    @RequestMapping(value = "/findAll")
    public Result findAll(){
        List<Menu> list = menuService.findAll();
        if(list!=null && list.size()>0){
            return new Result(true,MessageConstant.QUERY_MENU_LIST_SUCCESS,list);
        }
        else{
            return new Result(false,MessageConstant.QUERY_MENU_LIST_FAIL);
        }
    }
}
