package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.MenuDao;
import com.itheima.health.dao.UserDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CheckItemServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/28 10:03
 * @Version V1.0
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    UserDao userDao;

    @Autowired
    MenuDao menuDao;

    @Override
    public User findUserByUsername(String username) {
        User user = userDao.findUserByUsername(username);
        return user;
    }

    @Override
    public List<Map<String, Object>> getMenuList(String username) {
        User user = userDao.findUserByUsername(username);

        //根据 user.id 查一级菜单列表
        List<Menu> menuList = userDao.findMenuListByUserId(user.getId());

        List<Map<String,Object>> mapMenu = new ArrayList<>();
        for (Menu menu : menuList) {
            Map<String,Object> map = new HashMap<>();
            map.put("path",menu.getPath());
            map.put("title",menu.getName());
            map.put("icon",menu.getIcon());
            //查二级菜单
            List<Menu> menuChildrenList = userDao.findMenuListLevel2ByMenuId(menu.getId());

            List<Map<String,Object>> mapMenu1 = new ArrayList<>();
            if (menuChildrenList != null && menuChildrenList.size() > 0){
                for (Menu menu1 : menuChildrenList) {
                    Map<String,Object> map1 = new HashMap<>();
                    map1.put("path",menu1.getPath());
                    map1.put("title",menu1.getName());
                    map1.put("linkUrl",menu1.getLinkUrl());
                    mapMenu1.add(map1);
                }
            }
            map.put("children",mapMenu1);

            mapMenu.add(map);
        }
        return mapMenu;
    }

    /*@Override
    public List<Menu> getMenuListDemo02(String username) {
        //通过username获取roleId
        Integer roleId = userDao.findRoleIdByUsername(username);
        //通过roleId获取菜单，null表示传的参数parentMenuId为null
        List<Menu> menuList = menuDao.findMenuByRoleId(roleId, null);
        //遍历menuList，传入id通过parentmenuId查找子菜单
        for (Menu menu : menuList) {
            List<Menu> menuChildren = menuDao.findMenuByRoleId(roleId, menu.getId());
            System.out.println(menu.getPath());
            menu.setChildren(menuChildren);
        }
        //此处使用递归，当数据库增加多级目录时该方法依然适用
        *//*List<Menu> menuList = getMenu(roleId, null);*//*
        //数据库没有工作台这个菜单，需要自己添加到集合的首位
        Menu menu = new Menu();
        menu.setPath("1");
        menu.setName("工作台");
        menu.setIcon("fa-dashboard");
        menuList.add(0, menu);
        return menuList;
    }*/

   /* *//**
     * 使用递归查询菜单
     *
     *
     *//*
    public List<Menu> getMenu(Integer roleId, Integer parentmenuId) {
        //调用持久层方法获取当前级别菜单
        List<Menu> menuList = menuDao.findMenuByRoleId(roleId, parentmenuId);
        //遍历，判断是否有下一级
        if (menuList != null && menuList.size() > 0) {
            for (Menu menu : menuList) {
                List<Menu> menuChildren = getMenu(roleId, menu.getId());
                menu.setChildren(menuChildren);
            }
        }
        return menuList;
    }*/


    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        // 1：初始化分页的参数
        PageHelper.startPage(currentPage,pageSize);
        // 2：查询
        Page<CheckItem> page = userDao.findByCondition(queryString);
        // 3：封装PageResult数据
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    public List<Integer> findRoleIdsByCheckGroupId(Integer id) {
        return userDao.findRoleIdsByCheckGroupId(id) ;
    }
}
