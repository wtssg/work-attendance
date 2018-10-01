package wtssg.xdly.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import wtssg.xdly.user.entity.User;
import wtssg.xdly.user.services.UserService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/home")
    public String home() {
        return "home";
    }

    /**
     * 从session中获得用户信息
     * @param session
     * @return
     */
    @RequestMapping("/userinfo")
    @ResponseBody
    public User getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        return user;
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "login";
    }

    @RequestMapping("/sign")
    @ResponseBody
    public String signAttend(@RequestBody User user) throws Exception {
        userService.createUser(user);
        return "success";
    }

}
