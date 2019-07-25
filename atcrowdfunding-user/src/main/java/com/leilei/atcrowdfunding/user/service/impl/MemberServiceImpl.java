package com.leilei.atcrowdfunding.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.leilei.atcrowdfunding.user.mapper.TMemberMapper;
import com.leilei.front.vo.req.MemberRegisterVo;
import com.leilei.front.bean.TMember;
import com.leilei.front.bean.TMemberExample;
import com.leilei.front.constant.AppConstant;
import com.leilei.front.enume.AuthStatusEnume;
import com.leilei.front.enume.UserTypeEnume;
import com.leilei.front.exception.UserEmailException;
import com.leilei.front.exception.UserLoginacctException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;

import com.leilei.atcrowdfunding.user.service.MemberService;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    TMemberMapper memberMapper;

    @Override
    public void regist(MemberRegisterVo memberRegisterVo) {
        //1.检查手机号是否被占用
        String mobile = memberRegisterVo.getMobile();
        String email = memberRegisterVo.getEmail();
        //true代表可用
        boolean b = checkMobile(mobile);
        if (!b) {
            throw new UserLoginacctException();
        }
        //2.检查邮箱是否被占用
        boolean b1 = checkEmail(email);
        if (!b1) {
            throw new UserEmailException();
        }
        //3.将会员信息保存
        TMember member = new TMember();
        member.setLoginacct(mobile);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(memberRegisterVo.getPassword());
        member.setUserpswd(encode);
        member.setUsername(mobile);
        member.setEmail(memberRegisterVo.getEmail());

        //实名认证状态 0 - 未实名认证， 1 - 实名认证申请中， 2 - 已实名认证
        member.setAuthstatus(AuthStatusEnume.UNAUTH.getCode());
        // 用户类型: 0 - 个人， 1 - 企业
        member.setUsertype(UserTypeEnume.PERSONAL.getCode());
        member.setRealname("未命名");

        memberMapper.insertSelective(member);
    }

    @Override
    public TMember login(String loginacct, String password) {
        TMemberExample example = new TMemberExample();
        example.createCriteria().andLoginacctEqualTo(loginacct);
        List<TMember> tMembers = memberMapper.selectByExample(example);
        if (tMembers != null && tMembers.size() == 1) {
            TMember member = tMembers.get(0);
            //获得数据库的密码
            String userpswd = member.getUserpswd();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            //matches方法，第一个参数是明文密码，第二个参数是匿文密码
            boolean matches = encoder.matches(password, userpswd);
            if (matches) {
                return tMembers.get(0);
            } else {
                return null;
            }
        }
        return null;
    }

    //获取个人信息
    @Override
    public TMember getMemberInfo(String accessToken) {
        String jsonString = redisTemplate.opsForValue().get(AppConstant.MEMBER_LOGIN_CACHE_PREFIX+accessToken);
        TMember jsonMember = JSON.parseObject(jsonString, TMember.class);
        TMember member = memberMapper.selectByPrimaryKey(jsonMember.getId());
        return member;
    }

    private boolean checkEmail(String email) {
        TMemberExample example = new TMemberExample();
        example.createCriteria().andEmailEqualTo(email);
        long count = memberMapper.countByExample(example);
        return count == 0 ? true : false;
    }

    private boolean checkMobile(String mobile) {
        TMemberExample example = new TMemberExample();
        example.createCriteria().andLoginacctEqualTo(mobile);
        long count = memberMapper.countByExample(example);
        return count == 0 ? true : false;
    }
}
