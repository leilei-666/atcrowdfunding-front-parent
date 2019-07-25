package com.leilei.atcrowdfunding.app.service.feign;

import com.leilei.front.bean.TTag;
import com.leilei.front.bean.TType;
import com.leilei.front.common.AppResponse;
import com.leilei.front.vo.req.ProjectBaseInfoVo;
import com.leilei.front.vo.req.ProjectReturnVo;
import com.leilei.front.vo.resp.ProjectTempVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/project")
@FeignClient(value = "ATCROWDFUNDING-PROJECT")
public interface ProjectServiceFeign {
    @PostMapping("/create/init")
    public AppResponse<ProjectTempVo> init(@RequestParam("accessToken") String accessToken);

    @GetMapping("/sys/type")
    public AppResponse<List<TType>> sysType();

    @GetMapping("/sys/tags")
    public AppResponse<List<TTag>> sysTags();

    @PostMapping("/create/savebaseinfo")
    public AppResponse<String> saveBaseInfo(@RequestBody ProjectBaseInfoVo baseInfoVo);

    @PostMapping("/create/return")
    public AppResponse<String> addReturn(@RequestBody List<ProjectReturnVo> returns);
}
