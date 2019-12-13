package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import com.itheima.health.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @ClassName CheckItemServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/28 10:03
 * @Version V1.0
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService{

    @Autowired
    MemberDao memberDao;

    @Override
    public Member findMemberByTelephone(String telephone) {
        return memberDao.findMemberByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        // 如果密码不为空，使用md5进行加密
        if(member !=null && member.getPassword()!=null && !member.getPassword().equals("")){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByRegTime(List<String> months) {
        // 组织数据
        List<Integer> memberCounts = new ArrayList<>();
        // 遍历months
        if(months!=null && months.size()>0){
            // 格式：[2019-01, 2019-02, 2019-03, 2019-04, 2019-05, 2019-06, 2019-07, 2019-08, 2019-09, 2019-10, 2019-11, 2019-12]
            for (String month : months) {
                String sDate = month+"-31"; // 使用当前月，计算当前月的最后1天
                Integer count = memberDao.findMemberCountByRegTime(sDate);
                memberCounts.add(count);
            }
        }
        return memberCounts;
    }

    //通过月份查询每个月的会员数量
    @Override
    public List<Integer> findMonthMemberCountByRegTime(List<String> months) {
        // 组织数据
        List<Integer> memberCounts = new ArrayList<>();
        // 遍历months
        if(months!=null && months.size()>0){
            // 格式：[2019-01, 2019-02, 2019-03, 2019-04, 2019-05, 2019-06, 2019-07, 2019-08, 2019-09, 2019-10, 2019-11, 2019-12]
            for (String month : months) {
                String begin=month+"-1";//每个月的第一天
                String end = month+"-31"; // 使用当前月，计算当前月的最后1天
                Integer count = memberDao.findMonthMemberCountByRegTime(begin,end);
                memberCounts.add(count);
            }
        }
        return memberCounts;
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        // 1：初始化分页的参数
        PageHelper.startPage(currentPage,pageSize);
        // 2：查询
        Page<Member> page = memberDao.findByCondition(queryString);
        // 3：封装PageResult数据
        return new PageResult(page.getTotal(),page.getResult());
    }
    @Override
    public Map<String, Object> getMemberCountBySex() {
        //创建Map用于封装数据
        Map<String,Object> resultMap = new HashMap<>();
        //存放性别
        List<String> sexType = new ArrayList<>();
        //根据性别获取会员数量
        List<Map<String,Object>> list = memberDao.getMemberCountBySex();
        //遍历list
        for (Map<String, Object> map : list) {
            Set<String> key = map.keySet();
            for (String sex : key) {
                //根据sex对应的值
                String str = map.get(sex).toString();
                if ("1".equals(str)) {
                    //说明为男
                    map.put(sex,"男");
                    sexType.add("男");
                }else if ("2".equals(str)) {
                    //说明为女
                    map.put(sex,"女");
                    sexType.add("女");
                }
            }
        }
        resultMap.put("sexType",sexType);
        resultMap.put("memberCount",list);
        return resultMap;
    }

    @Override
    public Map<String, Object> getMemberReportAge() {
        //创建map集合，用于封装返回的数据
        Map<String,Object> resultMap = new HashMap<>();
        //用于存放年龄段
        List<String> ageList = new ArrayList<>();
        //根据年龄段查询各年龄段的人数
        List<Map<String,Object>> mapList = memberDao.getMemberReportAge();
        if (mapList != null) {
            for (Map<String, Object> map : mapList) {
                String name = String.valueOf(map.get("name"));
                ageList.add(name);
            }
        }
        resultMap.put("ageScope",ageList);
        resultMap.put("ageScopeCount",mapList);
        return resultMap;
    }

}
