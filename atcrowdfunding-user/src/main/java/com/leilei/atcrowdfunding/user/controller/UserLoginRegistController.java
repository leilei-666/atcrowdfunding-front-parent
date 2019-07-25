package com.leilei.atcrowdfunding.user.controller;

import com.alibaba.fastjson.JSON;
import com.leilei.atcrowdfunding.user.component.SmsTemplate;
import com.leilei.atcrowdfunding.user.service.MemberService;
import com.leilei.front.vo.req.MemberRegisterVo;
import com.leilei.front.vo.resp.MemberRespsonVo;
import com.leilei.front.bean.TMember;
import com.leilei.front.common.AppResponse;
import com.leilei.front.constant.AppConstant;
import com.leilei.front.exception.UserEmailException;
import com.leilei.front.exception.UserLoginacctException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Api(tags = "用户登录注册模块")
@RestController
@RequestMapping("/user")
@Slf4j
@CrossOrigin   //允许跨域
public class UserLoginRegistController {
    @Autowired
    SmsTemplate smsTemplate;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    MemberService memberService;


    @ApiOperation("用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginacct", value = "账号(手机号)", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true)
    })
    @PostMapping("/login")
    public AppResponse<MemberRespsonVo> userLogin(@RequestParam(value = "loginacct", required = true) String loginacct,
                                                  @RequestParam(value = "password", required = true) String password) {
        TMember member = memberService.login(loginacct, password);
        if (member == null) {
            return AppResponse.fail().msg("登录失败，账号密码错误");
        } else {
            MemberRespsonVo memberRespsonVo = new MemberRespsonVo();
            BeanUtils.copyProperties(member, memberRespsonVo);
            String accessToken = UUID.randomUUID().toString().replace("-", "");
            memberRespsonVo.setAccessToken(accessToken);
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            //将数据库查询到的数据放入redis缓存中，利用令牌机制，key为uuid，值使用fastjson转换为json串，
            ops.set(AppConstant.MEMBER_LOGIN_CACHE_PREFIX + accessToken, JSON.toJSONString(member), 30, TimeUnit.MINUTES);
            return AppResponse.success(memberRespsonVo);
        }
    }

    /*
    Controller负责收集数据，顶多做一个合法性判断（非空）
    业务逻辑判断在service中
    SpringCloud：
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public AppResponse<String> userRegister( MemberRegisterVo memberRegisterVo) {
        log.debug("{}用户正在注册", memberRegisterVo.getMobile());
        //校验
        if (StringUtils.isEmpty(memberRegisterVo.getCode())) {
            return AppResponse.fail().msg("验证码不正确");
        }
        if (StringUtils.isEmpty(memberRegisterVo.getEmail())) {
            return AppResponse.fail().msg("邮箱必填");
        }
        if (StringUtils.isEmpty(memberRegisterVo.getMobile())) {
            return AppResponse.fail().msg("手机号必填");
        }
        if (StringUtils.isEmpty(memberRegisterVo.getPassword())) {
            return  AppResponse.fail().msg("密码必填");
        }
        //校验验证码

        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String code = ops.get(AppConstant.CODE_CACHE_PREFIX + memberRegisterVo.getMobile());
        if (StringUtils.isEmpty(code)) {
            return AppResponse.fail().msg("验证码过期请重新获取");
        } else {
            if (!code.equalsIgnoreCase(memberRegisterVo.getCode())) {
                return AppResponse.fail().msg("验证码错误");

            } else {
                redisTemplate.delete(AppConstant.CODE_CACHE_PREFIX + memberRegisterVo.getMobile());
                try {
                    memberService.regist(memberRegisterVo);
                } catch (UserEmailException e) {
                    return AppResponse.fail().msg(e.getMessage());
                } catch (UserLoginacctException e) {
                    return AppResponse.fail().msg(e.getMessage());
                }
            }
        }


        return AppResponse.success("");
    }

    @ApiOperation("重置密码")
    @PostMapping("/reset")
    public String resetPassword() {
        return null;
    }

    @ApiOperation("获取短信验证码")
    @PostMapping("/sendsms")
    public AppResponse<String> sendSms(@RequestParam("mobile") String mobile) {
        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 5);
        smsTemplate.sendSms(mobile, code);

        //1、将验证码放在redis中，下一次进行验证； k（mobile）-v（code）；redis中的key一般都要设置过期时间
        //所有在redis中应该有前缀，每一个业务的前缀不一样；
        redisTemplate.opsForValue().set(AppConstant.CODE_CACHE_PREFIX + mobile, code, 5, TimeUnit.MINUTES);

        return AppResponse.success("").msg("短信发送完成");
    }

    @ApiOperation("验证短信验证码")
    @PostMapping("/valide")
    public String valideSms() {
        return null;
    }
}
