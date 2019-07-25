package com.leilei.atcrowdfunding.user.controller;

import com.leilei.atcrowdfunding.user.service.MemberService;
import com.leilei.front.bean.TMember;
import com.leilei.front.common.AppResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户个人信息模块")
@RestController
@RequestMapping("/user")
public class UserInfoController {
    @Autowired
    MemberService memberService;
    @Autowired
    StringRedisTemplate redisTemplate;

    @ApiOperation("获取个人信息")
    @GetMapping("/info")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken", value = "用户携带的token")
    })
    public AppResponse<TMember> getUserMessage(@RequestParam("accessToken") String accessToken) {
        TMember member = memberService.getMemberInfo(accessToken);
        member.setId(null);
        member.setUserpswd(null);
        return AppResponse.success(member);
    }

    @ApiOperation("更新个人信息")
    @PutMapping("/info")
    public String updateUserMessage() {
        return null;
    }

    @ApiOperation("获取用户收货地址")
    @GetMapping("/info/address")
    public String getUserAddress() {
        return null;
    }

    @ApiOperation("修改用户收货地址")
    @PutMapping("/info/address")
    public String updateUserAddress() {
        return null;
    }

    @ApiOperation("删除用户收货地址")
    @DeleteMapping("/info/address")
    public String delUserAddress() {
        return null;
    }

    @ApiOperation("获取我发起的项目")
    @GetMapping("/info/create/project")
    public String getMyProject() {
        return null;
    }

    @ApiOperation("获取我的系统消息")
    @GetMapping("/info/message")
    public String getMySystemMessage() {
        return null;
    }

    @ApiOperation("查看我的订单")
    @GetMapping("/info/order")
    public String getOrder() {
        return null;
    }

    @ApiOperation("删除我的订单")
    @DeleteMapping("/info/order")
    public String delOrder() {
        return null;
    }

    @ApiOperation("获取我关注的项目")
    @GetMapping("/info/star/project")
    public String getFocusprojects() {
        return null;
    }

    @ApiOperation("获取我支持的项目")
    @GetMapping("/info/support/project")
    public String getSupportProjects() {
        return null;
    }
}
