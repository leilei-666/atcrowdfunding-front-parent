package com.leilei.atcrowdfunding.app.controller;

import com.leilei.atcrowdfunding.app.component.OssTemplate;
import com.leilei.atcrowdfunding.app.service.feign.ProjectServiceFeign;
import com.leilei.front.bean.TTag;
import com.leilei.front.bean.TType;
import com.leilei.front.common.AppResponse;
import com.leilei.front.vo.req.BaseVo;
import com.leilei.front.vo.req.ProjectBaseInfoVo;
import com.leilei.front.vo.req.ProjectReturnVo;
import com.leilei.front.vo.resp.MemberRespsonVo;
import com.leilei.front.vo.resp.ProjectTempVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    ProjectServiceFeign projectService;
    @Autowired
    OssTemplate ossTemplate;

    /*
    第0步，发起众筹跳到阅读并同意协议
     */
    @RequestMapping("/start.html")
    public String startProject() {
        return "protected/project/start";
    }

    /*
        第1步，阅读并同意协议跳到项目及发起人信息
     */
    @RequestMapping("/start-step-1.html")
    public String start_step_1(HttpSession session, Model model) {
        //展示start_step_1之前
        //1.获取项目令牌
        MemberRespsonVo vo = (MemberRespsonVo) session.getAttribute("loginUser");
        String accessToken = vo.getAccessToken();
        AppResponse<ProjectTempVo> response = projectService.init(vo.getAccessToken());
        ProjectTempVo data = response.getData();
        session.setAttribute("projectToken", data.getProjectToken());
        //2.远程远程查询分类信息
        AppResponse<List<TType>> listAppResponse = projectService.sysType();
        //远程查询标签信息
        AppResponse<List<TTag>> listAppResponse1 = projectService.sysTags();
        List<TTag> alltags = listAppResponse1.getData();
        List<TTag> tags = new ArrayList<>();
        //整理标签的父子关系
        for (TTag tag : alltags) {
            //父标签
            if (tag.getPid() == 0) {
                for (TTag tTag : alltags) {
                    //父标签的子标签
                    if (tTag.getPid() == tag.getId()) {
                        tag.getChildrens().add(tTag);
                    }
                }
                tags.add(tag);
            }
        }
        //把分类和标签放入session域中
        model.addAttribute("sysTypes", listAppResponse.getData());
        model.addAttribute("sysTags", tags);
        return "protected/project/start-step-1";
    }

    /*
         第2步，项目及发起人信息跳到回报设置
     */
    @PostMapping("/start-step-2.html")
    public String start_step_2(ProjectBaseInfoVo vo,
                               @RequestParam("header") MultipartFile file,
                               @RequestParam("details") MultipartFile[] files,
                               HttpSession session, RedirectAttributes redirectAttributes) throws IOException {
        if (!file.isEmpty()) {
            //头图上传
            String url = ossTemplate.upload(file.getBytes(), file.getOriginalFilename());
            vo.setHeaderImage(url);
        }
        List<String> details=new ArrayList<>();
        if (files != null) {
            for (MultipartFile multipartFile : files) {
                if (!multipartFile.isEmpty()) {
                    String url = ossTemplate.upload(multipartFile.getBytes(), multipartFile.getOriginalFilename());
                    details.add(url);
                }
            }
        }
        vo.setDetailsImage(details);

        //将两个令牌封装完毕
        MemberRespsonVo loginUser = (MemberRespsonVo) session.getAttribute("loginUser");
        vo.setAccessToken(loginUser.getAccessToken());
        log.debug("AccessToken数据为{}", loginUser.getAccessToken());
        vo.setProjectToken((String) session.getAttribute("projectToken"));
        log.debug("ProjectToken数据为{}", session.getAttribute("projectToken"));
        log.debug("ProjectBaseInfoVo数据为{}", vo);
        //远程服务保存项目信息
        AppResponse<String> appResponse = projectService.saveBaseInfo(vo);
        if (appResponse.getCode()==0){
            return "redirect:/start-step-2.html";

        }else {
            //失败回到之前页面并进行回显
            redirectAttributes.addFlashAttribute("vo",vo);
            return "redirect:/start-step-1.html";
        }
    }

    /*
     第3步，回报设置跳到确认信息
     */
    @ResponseBody
    @PostMapping("/start-step-3.html")
    public AppResponse<String> start_step_3(@RequestBody List<ProjectReturnVo> returns,HttpSession session) {
        MemberRespsonVo loginUser = (MemberRespsonVo) session.getAttribute("loginUser");
        for (ProjectReturnVo aReturn : returns) {
            aReturn.setAccessToken(loginUser.getAccessToken());
            aReturn.setProjectToken((String) session.getAttribute("projectToken"));
        }
        AppResponse<String> appResponse = projectService.addReturn(returns);
        return appResponse;
    }

    /*
        第4步，确认信息跳到确认信息
     */
    @RequestMapping("/start-step-4.html")
    public String start_step_4(HttpSession session)
    {
        MemberRespsonVo loginUser = (MemberRespsonVo) session.getAttribute("loginUser");
        String accessToken = loginUser.getAccessToken();
        String projectToken = (String) session.getAttribute("projectToken");
        BaseVo baseVo=new BaseVo();
        baseVo.setAccessToken(accessToken);
        baseVo.setProjectToken(projectToken);
        AppResponse<String> submit = projectService.submit(baseVo);
        if (submit.getCode()==0){
            return "protected/project/start-step-4";

        }
        return "redirect:/start-step-3.html";
    }
}
