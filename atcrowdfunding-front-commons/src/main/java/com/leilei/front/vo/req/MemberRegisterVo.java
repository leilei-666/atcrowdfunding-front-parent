package com.leilei.front.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/*
封装注册提交来的vo数据
 */
@Data
@ApiModel
@ToString
public class MemberRegisterVo {
    @ApiModelProperty(value = "手机号",required = true)
    private String mobile;

    @ApiModelProperty(value = "密码",required = true)
    private String password;

    @ApiModelProperty(value = "邮箱",required = true)
    private String email;

    @ApiModelProperty(value = "验证码",required = true)
    private String code;
}
