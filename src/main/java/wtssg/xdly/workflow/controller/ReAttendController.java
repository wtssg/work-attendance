package wtssg.xdly.workflow.controller;

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
    public void startReAttendFlow(@RequestBody ReAttend reAttend, HttpSession session) {
//        User user = (User) session.getAttribute("userInfo");
        User user = new User();
        user.setUsername("xdly");
        reAttend.setReAttendStarter(user.getUsername());
        reAttendService.startReAttendFlow(reAttend);
    }

    @RequestMapping("/list")
    public String listReAttendFlow(Model model, HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
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
