package com.leilei.atcrowdfunding.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户实名审核模块")
@RestController
@RequestMapping("/user/auth")
public class UserRealAuthController {
    @ApiOperation("认证申请第二部-提交基本信息")
    @PostMapping("/baseinfo")
    public String submitBasicInformation() {
        return null;
    }

    @ApiOperation("认证申请第三部-上传资质信息")
    @PostMapping("/certs")
    public String uploadQualificationInformation() {
        return null;
    }

    @ApiOperation("获取需要上传的资质信息")
    @GetMapping("/certs2upload")
    public String getQualificationInformation() {
        return null;
    }

    @ApiOperation("认证申请第四部-确认邮箱信息")
    @PostMapping("/email")
    public String confirmEmailInformation() {
        return null;
    }

    @ApiOperation("认证申请第五部-用户认证申请开始")
    @GetMapping("/start")
    public String startAuth() {
        return null;
    }

    @ApiOperation("认证申请第一部-提交实名认证申请")
    @PostMapping("/submit")
    public String submitAuth() {
        return null;
    }

}
