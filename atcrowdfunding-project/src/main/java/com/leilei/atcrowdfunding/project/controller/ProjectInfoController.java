package com.leilei.atcrowdfunding.project.controller;

import com.leilei.atcrowdfunding.project.service.ProjectService;
import com.leilei.front.bean.TTag;
import com.leilei.front.bean.TType;
import com.leilei.front.common.AppResponse;
import com.leilei.front.vo.resp.ProjectAllInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "项目信息模块")
@RequestMapping("/project")
@RestController
public class ProjectInfoController {
    @Autowired
    ProjectService projectService;

    @ApiOperation("获取项目系统标签信息")
    @GetMapping("/sys/tags")
    public AppResponse<List<TTag>> sysTags() {


        List<TTag> tags = projectService.getSysTags();
        return AppResponse.success(tags);
    }

    @ApiOperation("获取项目总览列表")
    @GetMapping("/all/index")
    public AppResponse<List<ProjectAllInfoVo>> getAllIndex() {

        List<ProjectAllInfoVo> projects = projectService.getAllProjectsInfos();
        return AppResponse.success(projects);
    }

    @ApiOperation("获取项目系统分类信息")
    @GetMapping("/sys/type")
    public AppResponse<List<TType>> sysType() {

        List<TType> types = projectService.getSysTypes();
        return AppResponse.success(types);
    }
}
