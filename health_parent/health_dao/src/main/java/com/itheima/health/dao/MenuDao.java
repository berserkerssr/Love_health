package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Menu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface MenuDao {
    Page<CheckItem> findByCondition(String queryString);

    void add(Menu menu);

    List<Menu> findAll();

    Menu findById(Integer id);

    void edit(Menu menu);

    void deleteById(Integer id);

    List<Menu> findMenuByRoleId(@Param("roleId") Integer roleId, @Param("parentMenuId") Integer parentMenuId);
}
