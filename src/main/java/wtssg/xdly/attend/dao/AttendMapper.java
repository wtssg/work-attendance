package wtssg.xdly.attend.dao;

import wtssg.xdly.attend.entity.Attend;
import wtssg.xdly.vo.PageQueryCondition;

import java.util.List;

public interface AttendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Attend record);

    int insertSelective(Attend record);

    Attend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Attend record);

    int updateByPrimaryKey(Attend record);

    Attend selectTodayRecord(Long userId);

    int countByCondition(PageQueryCondition condition);

    List<Attend> selectAttendPage(PageQueryCondition condition);

    List<Long> selectTodayAbsence();
    void batchInsert(List<Attend> list);

    List<Attend> selectTodayEveningAbsence();
}