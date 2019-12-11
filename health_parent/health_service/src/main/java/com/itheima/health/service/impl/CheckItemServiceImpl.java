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

    // 不使用mybatis的分页插件PageHelper（传统方式）
    // 1：查找总记录数（sql：SELECT COUNT(*) FROM t_checkitem WHERE CODE = 'cz001' OR NAME = 'cz001' ）
    // Long count = checkItemDao.findCountByCondition(queryString);
    // 2：查询当前页显示的记录数（sql：SELECT * FROM t_checkitem WHERE CODE = 'cz001' OR NAME = 'cz001' LIMIT 0,10 ）
    //       limit关键字：参数一：当前页从第几条开始检索（0表示第1条）（计算公式：(currentPage-1)*pageSize）
    //                    参数二：当前页最多显示的记录数（计算公式：pageSize）
    // List<CheckItem> list = checkItemDao.findByCondition(queryString);
    // new PageResult(count,list);
    // 可以实现，问题1：麻烦；问题2：不能同通用，不能进行切换数据库
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        // 方案一：使用PageInfo完成封装
        // 使用Mybatis的分页插件（1：导入分页插件的坐标；2：在applicationContext-dao.xml中配置分页插件）
//        // 1：初始化分页的参数
//        PageHelper.startPage(currentPage,pageSize);
//        // 2：查询
//        List<CheckItem> list = checkItemDao.findByCondition(queryString);
//        // 3：封装PageResult数据
//        PageInfo<CheckItem> pageInfo = new PageInfo<>(list);
//        return new PageResult(pageInfo.getTotal(),pageInfo.getList());

        // 方案二：使用Page完成封装
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
