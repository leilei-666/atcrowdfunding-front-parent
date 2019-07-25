package com.leilei.atcrowdfunding.app.controller;

import com.leilei.atcrowdfunding.app.service.feign.UserServiceFeign;
import com.leilei.front.common.AppResponse;
import com.leilei.front.vo.req.MemberRegisterVo;
import com.leilei.front.vo.resp.MemberRespsonVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceFeign userService;

    @PostMapping("/login")
    public String doLogin(String loginacct, String password,
                          HttpSession session, Model model) {
        //回到首页
        AppResponse<MemberRespsonVo> login = userService.userLogin(loginacct, password);
        if (login.getCode() == 0) {
            //登录成功
            MemberRespsonVo loginUser = login.getData();
            session.setAttribute("loginUser", loginUser);
            //回到首页
            return "redirect:/index.html";

        } else {
            model.addAttribute("loginacct", loginacct);
            model.addAttribute("msg", login.getMsg());
            //回到登录页
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, Model model) {
        session.invalidate();
        return "redirect:/index.html";
    }

    //重定向携带数据
    @PostMapping("/reg")
    public String register(MemberRegisterVo vo, RedirectAttributes redirectAttributes) {
        System.out.println(vo);
        AppResponse<String> appResponse = userService.userRegister(vo);
        //cloud会把vo转换成json，导致没有写@RequestBody的服务就将json转不成对象
        if (appResponse.getCode() == 0) {
            //提示注册成功可以登录
            redirectAttributes.addFlashAttribute("msg", "注册成功，可以登录了");
            return "redirect:/login.html";

        } else {
            redirectAttributes.addFlashAttribute("msg", appResponse.getMsg());
            redirectAttributes.addFlashAttribute("vo", vo);
            return "redirect:/reg.html";

        }
    }
}
