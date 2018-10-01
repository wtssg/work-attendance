package wtssg.xdly.attend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import wtssg.xdly.attend.entity.Attend;
import wtssg.xdly.attend.services.AttendService;

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
}
