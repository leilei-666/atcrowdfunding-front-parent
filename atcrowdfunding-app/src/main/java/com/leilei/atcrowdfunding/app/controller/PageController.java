package com.leilei.atcrowdfunding.app.controller;

import com.leilei.atcrowdfunding.app.service.feign.ProjectServiceFeign;
import com.leilei.front.common.AppResponse;
import com.leilei.front.vo.resp.MemberRespsonVo;
import com.leilei.front.vo.resp.ProjectAllInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PageController {
    @Autowired
    ProjectServiceFeign projectServiceFeign;

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

    @GetMapping(value = {"/", "index.html"})
    public String indexPage(Model model) {
        AppResponse<List<ProjectAllInfoVo>> allIndex = projectServiceFeign.getAllIndex();
        model.addAttribute("projects", allIndex.getData());
        return "protected/index";
    }
}
