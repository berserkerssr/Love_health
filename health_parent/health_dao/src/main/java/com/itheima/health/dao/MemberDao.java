package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Member;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MemberDao {

    Member findMemberByTelephone(String telephone);

    void add(Member member);

    Integer findMemberCountByRegTime(String sDate);

    Integer findTodayNewMember(String reportDate);

    Integer findTotalMember();

    Integer findThisNewMember(String date);

    Integer findMonthMemberCountByRegTime(@Param("begin") String begin, @Param("end")String end);

    Page<Member> findByCondition(String queryString);

    List<Map<String, Object>> getMemberCountBySex();

    List<Map<String, Object>> getMemberReportAge();
}
