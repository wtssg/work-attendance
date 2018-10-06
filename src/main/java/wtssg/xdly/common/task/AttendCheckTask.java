package wtssg.xdly.common.task;

import org.springframework.beans.factory.annotation.Autowired;
import wtssg.xdly.attend.dao.AttendMapper;
import wtssg.xdly.attend.services.AttendService;

public class AttendCheckTask {

    @Autowired
    AttendService attendService;
    public void checkAttend(){
        attendService.checkAttend();
    };
}
