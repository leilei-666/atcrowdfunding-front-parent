package com.leilei.front.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class AppResponse<T> {
    @ApiModelProperty("请求是否成功状态码，0代表成功，其他代表失败")
    private Integer code;
    @ApiModelProperty("成功/失败的提示")
    private String msg;
    @ApiModelProperty("真正的数据")
    private T data;

    //失败
    public static <T> AppResponse<T> fail() {
        AppResponse<T> appResponse = new AppResponse<>();
        appResponse.setCode(1);
        appResponse.setMsg("操作失败");
        return appResponse;

    }

    //成功
    public static <T> AppResponse<T> success(T data) {
        AppResponse<T> appResponse = new AppResponse<>();
        appResponse.setCode(0);
        appResponse.setMsg("操作成功");
        appResponse.setData(data);
        return appResponse;
    }

    /*
    如何设计链式调用，总是返回这个对象
     */
    public AppResponse msg(String msg) {
        this.setMsg(msg);
        return this;
    }

    public AppResponse code(Integer code) {
        this.setCode(code);
        return this;
    }

    public AppResponse data(T data) {
        this.setData(data);
        return this;
    }
}
