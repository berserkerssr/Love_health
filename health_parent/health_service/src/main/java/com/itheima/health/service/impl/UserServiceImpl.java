package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.UserDao;
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
}
