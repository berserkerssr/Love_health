package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName CheckItemServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/28 10:03
 * @Version V1.0
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    CheckItemDao checkItemDao;


    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        // 1：初始化分页的参数
        PageHelper.startPage(currentPage,pageSize);
        // 2：查询
        Page<CheckItem> page = checkItemDao.findByCondition(queryString);
        // 3：封装PageResult数据
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 删除：考虑到主外键
     * 如果中间表中存在数据，怎么办？
     * 方案一：删除检查项的时候，判断中间表中是否存在数据，如果存在数据，将中间表的数据先删除，再删除检查项的表
     * 方案二：删除检查项的时候，判断中间表中是否存在数据，如果存在数据，提示“当前检查项在中间表中存在数据不能删除”
     * @param id
     */
    @Override
    public void deleteById(Integer id) {
        // 使用检查项的id，查询中间表，判断是否存在数据
        long count = checkItemDao.findCheckGroupAndCheckItemCountByCheckItemId(id);
        // 存在数据
        if(count>0){
            throw new RuntimeException("当前检查项在中间表中存在数据不能删除！");
        }
        // 删除检查项
        checkItemDao.deleteById(id);
    }

    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
