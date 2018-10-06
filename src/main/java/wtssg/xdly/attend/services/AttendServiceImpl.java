package wtssg.xdly.attend.services;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wtssg.xdly.attend.dao.AttendMapper;
import wtssg.xdly.attend.entity.Attend;
import wtssg.xdly.common.page.PageQueryBean;
import wtssg.xdly.common.utils.DateUtils;
import wtssg.xdly.vo.PageQueryCondition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 打卡业务
 *@author wt
 *@date 2018/10/3 1:59
 *@description
 */
@Service("AttendServiceImpl")
public class AttendServiceImpl implements AttendService {
    /**
     * 12:00的小时数和分钟数
     */
    private static final int NOON_HOUR = 12;
    private static final int NOON_MINUTE = 0;

    /**
     * 缺勤一整天的缺勤分钟数
     */
    private static final Integer ABSENCE_DAY = 480;

    /**
     * 考勤状态，2为异常，1为正常
     */
    private static final Byte ATTEND_STATUS_NORMAL = 1;
    private static final Byte ATTEND_STATUS_ABNORMAL = 2;

    private static final int MORNING_HOUR = 9;
    private static final int MORNING_MINUTE = 30;
    private static final int EVENING_HOUR = 18;
    private static final int EVENING_MINUTE = 30;

    private Log log = LogFactory.getLog(AttendServiceImpl.class);

    @Autowired
    private AttendMapper attendMapper;

    @Override
    public void signAttend(Attend attend) throws Exception {
        try {
            Date today = new Date();
            attend.setAttendDate(today);
            attend.setAttendWeek((byte) DateUtils.getWeek(today));
            // 查询此人当天是否有打卡记录
            Attend todayRecord =attendMapper.selectTodayRecord(attend.getUserId());
            Date noon = DateUtils.getDate(NOON_HOUR, NOON_MINUTE);
            Date morningAttend = DateUtils.getDate(MORNING_HOUR, MORNING_MINUTE);
            if (todayRecord == null) {
                // 当天没有打卡记录
                if (today.compareTo(noon) <= 0) {
                    // 打卡时间早于12点，早上打卡
                    attend.setAttendMoring(today);
                    if (today.compareTo(morningAttend) > 0) {
                        // 大于九点半迟到了
                        attend.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                        attend.setAbsence(DateUtils.getMinute(morningAttend, today));
                    } else {
                        // 没有迟到
                        attend.setAbsence(0);
                        attend.setAttendStatus(ATTEND_STATUS_NORMAL);
                    }
                } else {
                    // 打卡异常，早上没有打卡，打卡时间晚于12点，晚上打卡
                    attend.setAttendEvening(today);
                    attend.setAbsence(ABSENCE_DAY);
                    attend.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                }
                attendMapper.insertSelective(attend);
            } else {
                // 有打卡记录
                if (today.compareTo(noon) <= 0) {
                    // 打卡时间早于12点，已经有打卡记录，不覆盖打卡记录
                    return;
                } else {
                    // 打卡时间晚于12点，已经有打卡记录，覆盖打卡记录，晚上打卡
                    todayRecord.setAttendEvening(today);
                    Date eveningAttend = DateUtils.getDate(EVENING_HOUR,EVENING_MINUTE);
                    if (todayRecord.getAttendMoring() != null) {
                        if (today.compareTo(eveningAttend) < 0) {
                            //早于18点半，早退
                            todayRecord.setAbsence(todayRecord.getAbsence() + DateUtils.getMinute(today,eveningAttend));
                            todayRecord.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                        } else {
                            if (todayRecord.getAttendMoring().compareTo(morningAttend) < 0) {
                                todayRecord.setAttendStatus(ATTEND_STATUS_NORMAL);
                                todayRecord.setAbsence(0);
                            } else {
                                todayRecord.setAbsence(DateUtils.getMinute(morningAttend, todayRecord.getAttendMoring()));
                            }
                        }
                    }
                    attendMapper.updateByPrimaryKeySelective(todayRecord);
                }
            }
        } catch (Exception e) {
            log.error("用户签到异常", e);
            throw e;
        }
    }

    @Override
    public PageQueryBean listAttend(PageQueryCondition condition) {
        // 根据条件查询，得到记录数
        int count = attendMapper.countByCondition(condition);
        PageQueryBean pageResult = new PageQueryBean();
        if (count > 0) {
            pageResult.setTotalRows(count);
            pageResult.setCurrentPage(condition.getCurrentPage());
            pageResult.setPageSize(condition.getPageSize());
            List<Attend> attendList = attendMapper.selectAttendPage(condition);
            pageResult.setItems(attendList);
        }
        return pageResult;
    }

    @Override
    public void checkAttend() {
        //查询缺勤用户ID 插入打卡记录  并且设置为异常 缺勤480分钟
        List<Long> userIdList = attendMapper.selectTodayAbsence();
        if (CollectionUtils.isNotEmpty(userIdList)) {
            List<Attend> attendList = new ArrayList<Attend>();
            for (long userId : userIdList) {
                Attend attend = new Attend();
                attend.setUserId(userId);
                attend.setAttendDate(new Date());
                attend.setAttendWeek((byte) DateUtils.getTodayWeek());
                attend.setAbsence(ABSENCE_DAY);
                attend.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                attendList.add(attend);
            }
            // 将打卡记录批量插入数据库
            attendMapper.batchInsert(attendList);
        }

        // 检查晚打卡 将下班未打卡记录设置为异常
        List<Attend> attendList = attendMapper.selectTodayEveningAbsence();
        if (CollectionUtils.isNotEmpty(attendList)) {
            for (Attend attend : attendList) {
                attend.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                attend.setAbsence(ABSENCE_DAY);
                attendMapper.updateByPrimaryKeySelective(attend);
            }
        }
    }
}
