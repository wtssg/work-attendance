package wtssg.xdly.attend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import wtssg.xdly.attend.entity.Attend;
import wtssg.xdly.attend.services.AttendService;
import wtssg.xdly.common.page.PageQueryBean;
import wtssg.xdly.user.entity.User;
import wtssg.xdly.vo.PageQueryCondition;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("attend")
public class AttendController {

    @Autowired
    private AttendService attendService;

    @RequestMapping
    public String toAttend() {
        return "attend";
    }

    @RequestMapping("/sign")
    @ResponseBody
    public String signAttend(@RequestBody Attend attend) throws Exception {
        attendService.signAttend(attend);
        return "success";
    }

    /**
     * 考勤数据分页查询
     * @param condition
     * @return
     */
    @RequestMapping("/attendlist")
    @ResponseBody
    public PageQueryBean listAttend(PageQueryCondition condition, HttpSession session) {

        User user = (User) session.getAttribute("userInfo");
        String[] rangeDate = condition.getRangeDate().split("/");
        condition.setStartDate(rangeDate[0]);
        condition.setEndDate(rangeDate[1]);
        condition.setUserId(user.getId());
        PageQueryBean result = attendService.listAttend(condition);
        return result;

    }
}
