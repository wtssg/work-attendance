package wtssg.xdly.attend.services;

import wtssg.xdly.attend.entity.Attend;
import wtssg.xdly.common.page.PageQueryBean;
import wtssg.xdly.vo.PageQueryCondition;

public interface AttendService {
    void signAttend(Attend attend) throws Exception;

    PageQueryBean listAttend(PageQueryCondition condition);

    void checkAttend();
}
