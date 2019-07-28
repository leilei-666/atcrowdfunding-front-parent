package com.leilei.atcrowdfunding.project.service;

import com.leilei.front.bean.TProject;
import com.leilei.front.vo.req.BaseVo;
import com.leilei.front.vo.req.ProjectBaseInfoVo;
import com.leilei.front.vo.req.ProjectReturnVo;
import com.leilei.front.bean.TTag;
import com.leilei.front.bean.TType;
import com.leilei.front.vo.resp.ProjectAllInfoVo;

import java.util.List;

public interface ProjectService {
    String initProject(String accessToken);

    List<TTag> getSysTags();

    List<TType> getSysTypes();

    boolean saveTempBaseInfo(ProjectBaseInfoVo baseInfoVo);

    boolean saveTempReturn(List<ProjectReturnVo> returns);

    void submitProjectToDb(BaseVo vo);

    void tempProjectToDb(BaseVo vo);


    List<TProject> getAllProjects();

    List<ProjectAllInfoVo> getAllProjectsInfos();

}
