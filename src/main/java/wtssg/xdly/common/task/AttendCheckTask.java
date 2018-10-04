package wtssg.xdly.common.task;

import org.springframework.beans.factory.annotation.Autowired;
import wtssg.xdly.attend.dao.AttendMapper;

public class AttendCheckTask {

    @Autowired
    AttendMapper attendMapper;
    public void checkAttend(){
        System.out.println("task");
    };
}
