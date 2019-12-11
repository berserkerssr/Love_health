package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    CheckGroupDao checkGroupDao;


    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 1:基本信息【保存】 向t_checkgroup表新增一条数据，同时将新增数据的检查组的id封装到checkGroup中的id属性
        checkGroupDao.add(checkGroup);
        // 2:检查项信息【保存】 根据所选择的检查项数组（int），和检查组的id，向t_checkgroup_checkitem表新增数据
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        // 1：初始化PageHelper
        PageHelper.startPage(currentPage,pageSize);
        // 2：查询
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryString);
        // 3：封装结果数据PageResult
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 一：根据检查组页面填写情况，更新检查组数据，更新t_checkgroup
        checkGroupDao.edit(checkGroup);
        // 二：操作中间表数据t_checkgroup_checkitem
        // 1：使用检查组的id，先删除中间表数据
        checkGroupDao.deleteCheckGroupAndCheckItemByCheckGroupId(checkGroup.getId());
        // 2：使用检查组id和传递的检查项id的数组，向中间表中添加数据
        this.setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);

    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    // 根据所选择的检查项数组（int），和检查组的id，向t_checkgroup_checkitem表新增数据
    private void setCheckGroupAndCheckItem(Integer checkgroupId, Integer[] checkitemIds) {
        if(checkitemIds!=null && checkitemIds.length>0){
            for (Integer checkitemId : checkitemIds) {
                // 方案一：使用@Param指定参数的名称
                // checkGroupDao.addCheckGroupAndCheckItem(checkgroupId,checkitemId);
                // 方案二：使用Map
                Map<String,Object> map = new HashMap<>();
                map.put("checkgroupId",checkgroupId);
                map.put("checkitemId",checkitemId);
                checkGroupDao.addCheckGroupAndCheckItem(map);
            }
        }
    }
}
