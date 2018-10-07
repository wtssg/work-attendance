package wtssg.xdly.workflow.service;

import wtssg.xdly.workflow.entity.ReAttend;

import java.util.List;

public interface ReAttendService {
    List<ReAttend> listReAttend(String username);

    void startReAttendFlow(ReAttend reAttend);

    List<ReAttend> listTasks(String userName);

    void approve(ReAttend reAttend);
}
