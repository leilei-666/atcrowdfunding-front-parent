package com.leilei.atcrowdfunding.project.controller;

import com.leilei.atcrowdfunding.project.component.OssTemplate;
import com.leilei.atcrowdfunding.project.service.ProjectService;
import com.leilei.front.vo.req.BaseVo;
import com.leilei.front.vo.req.ProjectBaseInfoVo;
import com.leilei.front.vo.req.ProjectReturnVo;
import com.leilei.front.vo.resp.ProjectTempVo;
import com.leilei.front.common.AppResponse;
import com.leilei.front.constant.AppConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "项目发起模块")
@RequestMapping("/project/create")
@RestController
@Slf4j
public class ProjectCreateController {
    @Autowired
    ProjectService projectService;
    @Autowired
    OssTemplate ossTemplate;
    @Autowired
    StringRedisTemplate redisTemplate;

    @ApiOperation("项目创建第1步-项目初始创建")
    @PostMapping("/init")
    public AppResponse<ProjectTempVo> init(@RequestParam("accessToken") String accessToken) {
        //点击阅读并同意协议，新创一个临时项目；生成一个项目的临时Token；

        //返回刚创建的项目的临时令牌
        String token = projectService.initProject(accessToken);
        if (StringUtils.isEmpty(token)) {
            return AppResponse.fail().msg("项目创建异常，请确认登录状态");
        }
        ProjectTempVo projectTempVo = new ProjectTempVo();
        projectTempVo.setProjectToken(token);
        return AppResponse.success(projectTempVo);
    }


    @ApiOperation("项目创建第2步-基本信息保存")
    @PostMapping("/savebaseinfo")
    public AppResponse<String> saveBaseInfo(@RequestBody ProjectBaseInfoVo baseInfoVo) {
        System.out.println(baseInfoVo);
        boolean b=projectService.saveTempBaseInfo(baseInfoVo);
        if (b){
            return AppResponse.success("");
        }
        return null;
    }

    /**
     * StringMvc
     * 1.可以将返回的对象写成json
     * 2.可以将提交的json字符串逆转为对象，放在请求体中
     * 3.提交数据的时候，必须post方式提交，提交的数据必须是json
     */
    @ApiOperation("项目创建第3步-添加项目回报档位")
    @PostMapping("/return")
    public AppResponse<String> addReturn(@RequestBody List<ProjectReturnVo> returns){
        boolean b=projectService.saveTempReturn(returns);
    return AppResponse.success("成功").msg("数据保存成功");
    }

    /**
     * 浏览器想要文件上传的几个要素
     * 1.post请求
     * 2.<form method="post" enctype="multipart/form-data">
     * <input type="file" name="files" multiple> //写multiple表示一次可以上传多个，不加每个input标签只能上传一个
     * </form>
     * <p>
     * 服务器端：
     * 参数类型MultipartFile，默认name为file若不是加@RequestParam
     */
    @ApiOperation("项目图片上传")
    @PostMapping("/update")
    public AppResponse<List<String>> update(@RequestParam("file") MultipartFile[] file,
                                            @RequestParam("accessToken") String accessToken) throws IOException {
        Boolean hasKey = redisTemplate.hasKey(AppConstant.MEMBER_LOGIN_CACHE_PREFIX + accessToken);
        if (!hasKey) {
            return AppResponse.fail().msg("请先登录");
        }
        List<String> urls = new ArrayList<>();
        if (file != null) {
            for (MultipartFile multipartFile : file) {
                //判断当前文件是否为空
                if (!multipartFile.isEmpty()) {
                    //获得文件流
                    byte[] bytes = multipartFile.getBytes();
                    //获得文件名
                    String filename = multipartFile.getOriginalFilename();
                    //文件上传
                    try {
                        String upload = ossTemplate.upload(bytes, filename);
                        urls.add(upload);
                    } catch (IOException e) {

                    }
                }
            }
        }
        return AppResponse.success(urls);
    }


    @ApiOperation("项目提交审核申请")
    @PostMapping("/submit")
    public AppResponse<String> submit(BaseVo vo){
        projectService.submitProjectToDb(vo);
        return AppResponse.success("");
    }


    @ApiOperation("项目草稿保存")
    @PostMapping("/tempsave")
    public AppResponse<String> tempsave(BaseVo vo){
        projectService.tempProjectToDb(vo);
        return AppResponse.success("");
    }
}
