package wtssg.xdly.attend.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wtssg.xdly.attend.dao.AttendMapper;
import wtssg.xdly.attend.entity.Attend;

@Service("AttendServiceImpl")
public class AttendServiceImpl implements AttendService {
    private Log log = LogFactory.getLog(AttendServiceImpl.class);

    @Autowired
    private AttendMapper attendMapper;

    @Override
    public void signAttend(Attend attend) throws Exception {
        try {
            attendMapper.insertSelective(attend);
        } catch (Exception e) {
            log.error("用户签到异常", e);
            throw e;
        }
    }
}
