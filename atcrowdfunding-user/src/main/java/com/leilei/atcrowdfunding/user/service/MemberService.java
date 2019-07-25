package com.leilei.atcrowdfunding.user.service;

import com.leilei.front.vo.req.MemberRegisterVo;
import com.leilei.front.bean.TMember;
import com.leilei.front.exception.UserEmailException;
import com.leilei.front.exception.UserLoginacctException;

public interface MemberService {
    void regist(MemberRegisterVo memberRegisterVo) throws UserEmailException, UserLoginacctException;

    TMember login(String loginacct, String password);

    TMember getMemberInfo(String accessToken);
}
