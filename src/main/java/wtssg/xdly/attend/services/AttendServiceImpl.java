package wtssg.xdly.attend.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wtssg.xdly.attend.dao.AttendMapper;
import wtssg.xdly.attend.entity.Attend;
import wtssg.xdly.common.page.PageQueryBean;
import wtssg.xdly.common.utils.DateUtils;
import wtssg.xdly.vo.PageQueryCondition;

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
     * 12:00的小时数
     */
    private static final int NOON_HOUR = 12;
    /**
     * 12:00的分钟数
     */
    private static final int NOON_MINUTE = 0;
    private Log log = LogFactory.getLog(AttendServiceImpl.class);

    @Autowired
    private AttendMapper attendMapper;

    @Override
    public void signAttend(Attend attend) throws Exception {
        try {
            Date today = new Date();
            attend.setAttendDate(today);
            attend.setAttendWeek((byte) DateUtils.getWeek(today));
            Attend todayRecord =attendMapper.selectTodayRecord(attend.getUserId());
            Date noon = DateUtils.getDate(NOON_HOUR, NOON_MINUTE);
            if (todayRecord == null) {
                if (today.compareTo(noon) <= 0) {
                    // 打卡时间早于12点，早上打卡
                    attend.setAttendMoring(today);
                } else {
                    // 打卡时间晚于12点，晚上打卡
                    attend.setAttendEvening(today);
                }
                attendMapper.insertSelective(attend);
            } else {
                if (today.compareTo(noon) <= 0) {
                    // 打卡时间早于12点，已经有打卡记录，不覆盖打卡记录

                } else {
                    // 打卡时间晚于12点，已经有打卡记录，覆盖打卡记录，晚上打卡
                    todayRecord.setAttendEvening(today);
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
}
