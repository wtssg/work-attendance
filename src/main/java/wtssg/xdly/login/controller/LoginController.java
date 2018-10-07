package wtssg.xdly.login.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import wtssg.xdly.common.utils.MD5Utils;
import wtssg.xdly.user.entity.User;
import wtssg.xdly.user.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


/**
 *@author wt
 *@date 2018/9/28 0:57
 *@description 登录控制器
 */

@Controller
@RequestMapping("login")
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping
    public String login() {
        return "login";
    }

    @RequestMapping("/check")
    /*ResponseBody作用：将controller的方法返回的对象通过适当的转换器转换为指定的格式之后，
     *写入到response对象的body区，比如json或者xml，此时viewResolver失效
     */
    @ResponseBody
    public String checkLogin(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordToken userToken = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        subject.login(userToken);
        SecurityUtils.getSubject().getSession().setTimeout(1800000);
        return "login_success";
    }
//    public String checkLogin(HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//        User user = userService.findUserByUsername(username);
//        if (user != null) {
//            boolean flag = MD5Utils.checkPassword(password, user.getPassword());
//            if (flag) {
//                // 用户验证成功，设置session
//                request.getSession().setAttribute("userInfo", user);
//                return "login_success";
//            } else {
//                // 用户密码错误
//                return "login_fail";
//
//            }
//        } else {
//            // 用户不存在
//            return "login_fail";
//        }
//    }

    @RequestMapping("/register")
    public String Register(@RequestBody User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        userService.createUser(user);
        return "success";
    }
}
