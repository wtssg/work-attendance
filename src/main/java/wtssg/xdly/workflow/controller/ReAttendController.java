package wtssg.xdly.workflow.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import wtssg.xdly.user.entity.User;
import wtssg.xdly.workflow.entity.ReAttend;
import wtssg.xdly.workflow.service.ReAttendService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("reAttend")
public class ReAttendController {
    @Autowired
    ReAttendService reAttendService;

    @RequestMapping
    public String toReAttend(Model model, HttpSession session){
        User user = (User) session.getAttribute("userInfo");
        List<ReAttend> reAttendList = reAttendService.listReAttend(user.getUsername());
        model.addAttribute("reAttendList",reAttendList);
        return "reAttend";
    }

    @RequestMapping("/start")
    public void startReAttendFlow(@RequestBody ReAttend reAttend) {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("useInfo");
        reAttend.setReAttendStarter(user.getUsername());
        reAttendService.startReAttendFlow(reAttend);
    }

//    @RequiresRoles("leader")
    @RequiresPermissions("reAttend:list")
    @RequestMapping("/list")
    public String listReAttendFlow(Model model) {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("useInfo");
        String userName = user.getUsername();
        List<ReAttend> tasks = reAttendService.listTasks(userName);
        model.addAttribute(tasks);
        return "reAttendApprove";
    }

    @RequestMapping("/approve")
    public void approveReAttendFlow(@RequestBody ReAttend reAttend) {
        reAttendService.approve(reAttend);
    }
}
