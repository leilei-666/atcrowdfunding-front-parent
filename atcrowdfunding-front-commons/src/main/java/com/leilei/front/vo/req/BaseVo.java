package com.leilei.front.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@ApiModel
@Data
@ToString
public class BaseVo {


    @ApiModelProperty(value = "访问令牌",required = true)
    private String accessToken;//访问令牌
    @ApiModelProperty(value = "项目临时令牌",required = true)
    private String projectToken;//项目的临时token；项目的唯一标识
}
