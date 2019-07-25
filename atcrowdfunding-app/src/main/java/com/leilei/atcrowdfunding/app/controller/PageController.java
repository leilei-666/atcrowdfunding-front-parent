package com.leilei.atcrowdfunding.app.controller;

import com.leilei.front.vo.resp.MemberRespsonVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class PageController {
    @GetMapping("/member.html")
    public String memberPage(HttpSession session, RedirectAttributes redirectAttributes) {
        MemberRespsonVo loginUser = (MemberRespsonVo) session.getAttribute("loginUser");
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("msg", "请先登录");
            return "redirect:/login.html";
        }
        return "protected/member";
    }

    @GetMapping("/minecrowdfunding.html")
    public String minecrowdfundingPage(HttpSession session, RedirectAttributes redirectAttributes) {
        MemberRespsonVo loginUser = (MemberRespsonVo) session.getAttribute("loginUser");
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("msg", "请先登录");
            return "redirect:/login.html";
        }
        return "protected/member/minecrowdfunding";
    }
}
