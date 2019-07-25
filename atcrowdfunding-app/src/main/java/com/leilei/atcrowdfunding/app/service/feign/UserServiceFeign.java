package com.leilei.atcrowdfunding.app.service.feign;

import com.leilei.atcrowdfunding.app.config.FeignConfig;
import com.leilei.front.common.AppResponse;
import com.leilei.front.vo.req.MemberRegisterVo;
import com.leilei.front.vo.resp.MemberRespsonVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
    这个UserServiceFeign使用configuration指定的FeignConfig
    不指定就是使用SpringCloud默认配置，传输数据用json
 */
@FeignClient(value = "ATCROWDFUNDING-USER", configuration = FeignConfig.class)
public interface UserServiceFeign {
    /**
     * Http+Json；
     *
     *
     * 告诉SpringCloud远程服务，我们是x-www-form-urlencoded;
     * consumes：指定让SpringCloud编码成表单的k=v发出去，而不是json；
     * SpringCloud默认支持转k=v,但是没配置。
     */
    @PostMapping(value = "/user/register", consumes = "application/x-www-form-urlencoded")
    public AppResponse<String> userRegister(MemberRegisterVo memberRegisterVo);

    @PostMapping("/user/login")
    public AppResponse<MemberRespsonVo> userLogin(@RequestParam(value = "loginacct", required = true) String loginacct,
                                                  @RequestParam(value = "password", required = true) String password);

}
