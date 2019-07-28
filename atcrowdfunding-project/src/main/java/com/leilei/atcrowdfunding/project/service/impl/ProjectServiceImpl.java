package com.leilei.atcrowdfunding.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.leilei.atcrowdfunding.project.mapper.*;
import com.leilei.atcrowdfunding.project.service.ProjectService;
import com.leilei.front.vo.ProjectRedisStorageVo;
import com.leilei.front.vo.req.BaseVo;
import com.leilei.front.vo.req.ProjectBaseInfoVo;
import com.leilei.front.vo.req.ProjectReturnVo;
import com.leilei.front.bean.*;
import com.leilei.front.constant.AppConstant;
import com.leilei.front.enume.ImgTypeEnume;
import com.leilei.front.enume.ProjectStatusEnume;
import com.leilei.front.vo.resp.ProjectAllInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    TTagMapper tagMapper;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    TTypeMapper typeMapper;
    @Autowired
    TProjectMapper tProjectMapper;
    @Autowired
    TProjectImagesMapper tProjectImagesMapper;

    @Autowired
    TProjectTagMapper tProjectTagMapper;

    @Autowired
    TProjectTypeMapper tProjectTypeMapper;

    @Autowired
    TReturnMapper tReturnMapper;

    @Override
    public String initProject(String accessToken) {
        String memberJson = redisTemplate.opsForValue().get(AppConstant.MEMBER_LOGIN_CACHE_PREFIX + accessToken);
        TMember member = JSON.parseObject(memberJson, TMember.class);
        if (member == null) {
            return null;
        }
        /**
         *     private String accessToken;//访问令牌
         *     private String projectToken;//项目的临时token；项目的唯一标识
         *     private Integer memberid;//会员id
         */
        ProjectRedisStorageVo projectRedisStorageVo = new ProjectRedisStorageVo();
        projectRedisStorageVo.setAccessToken(accessToken);
        String projectToken = UUID.randomUUID().toString().replace("-", "");
        projectRedisStorageVo.setProjectToken(projectToken);
        projectRedisStorageVo.setMemberid(member.getId());
        String s = JSON.toJSONString(projectRedisStorageVo);
        //将项目基本信息，保存到redis中
        redisTemplate.opsForValue().set(AppConstant.PROJECT_TEMP_CACHE_PREFIX + projectToken, s);
        return projectToken;
    }

    @Override
    public List<TTag> getSysTags() {
        List<TTag> tTags = tagMapper.selectByExample(null);
        return tTags;
    }

    @Override
    public List<TType> getSysTypes() {
        List<TType> tTypes = typeMapper.selectByExample(null);
        return tTypes;
    }

    @Override
    public boolean saveTempBaseInfo(ProjectBaseInfoVo baseInfoVo) {
        String projectToken = baseInfoVo.getProjectToken();
        //通过当前项目的token，获取上一次访问的数据
        String s = redisTemplate.opsForValue().get(AppConstant.PROJECT_TEMP_CACHE_PREFIX + projectToken);
        //将上一次的json串转换为一个大VO对象
        ProjectRedisStorageVo projectRedisStorageVo = JSON.parseObject(s, ProjectRedisStorageVo.class);
        //将此次数据拷贝到大VO对象中
        BeanUtils.copyProperties(baseInfoVo, projectRedisStorageVo);
        //将大VO对象转换为json串
        String jsonString = JSON.toJSONString(projectRedisStorageVo);
        //放入缓存中重新进行保存
        redisTemplate.opsForValue().set(AppConstant.PROJECT_TEMP_CACHE_PREFIX + projectToken, jsonString);
        return true;
    }

    @Override
    public boolean saveTempReturn(List<ProjectReturnVo> returns) {
        String projectToken = returns.get(0).getProjectToken();
        String s = redisTemplate.opsForValue().get(AppConstant.PROJECT_TEMP_CACHE_PREFIX + projectToken);
        ProjectRedisStorageVo projectRedisStorageVo = JSON.parseObject(s, ProjectRedisStorageVo.class);
        List<TReturn> list = new ArrayList<>();
        for (ProjectReturnVo aReturn : returns) {
            TReturn tReturn = new TReturn();
            BeanUtils.copyProperties(aReturn, tReturn);
            list.add(tReturn);
        }
        projectRedisStorageVo.setProjectReturns(list);
        //将大VO对象转换为json串
        String jsonString = JSON.toJSONString(projectRedisStorageVo);
        //放入缓存中重新进行保存
        redisTemplate.opsForValue().set(AppConstant.PROJECT_TEMP_CACHE_PREFIX + projectToken, jsonString);
        return true;
    }

    //保存
    @Override
    public void submitProjectToDb(BaseVo vo) {
        saveProject(vo,ProjectStatusEnume.SUBMIT_AUTH);


    }
    @Transactional
    public void saveProject(BaseVo vo,ProjectStatusEnume statusEnume) {
        //1.保存项目基本数据
        String s = redisTemplate.opsForValue().get(AppConstant.PROJECT_TEMP_CACHE_PREFIX + vo.getProjectToken());
        ProjectRedisStorageVo projectRedisStorageVo = JSON.parseObject(s, ProjectRedisStorageVo.class);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TProject project = new TProject();
        BeanUtils.copyProperties(projectRedisStorageVo, project);
        project.setStatus(statusEnume.getCode());
        project.setCreatedate(simpleDateFormat.format(new Date()));
        //在mybatis中设置获取自增id
        tProjectMapper.insertSelective(project);
        //保存了项目之后，获取项目的id，封装image时会使用
        Integer id = project.getId();

        //2.1保存头图
        TProjectImages tProjectImages = new TProjectImages();
        String headerImage = projectRedisStorageVo.getHeaderImage();
        tProjectImages.setImgurl(headerImage);
        tProjectImages.setImgtype(ImgTypeEnume.HEADER_IMG.getCode());
        tProjectImages.setProjectid(id);
        tProjectImagesMapper.insertSelective(tProjectImages);
        //2.2保存详情图
        List<String> detailsImages = projectRedisStorageVo.getDetailsImage();
        detailsImages.forEach((url) -> {
            TProjectImages detailsImage = new TProjectImages();
            detailsImage.setImgurl(url);
            detailsImage.setImgtype(ImgTypeEnume.DETAIL_IMG.getCode());
            detailsImage.setProjectid(id);
            tProjectImagesMapper.insertSelective(detailsImage);
            //3.保存项目标签
            List<Integer> tagids = projectRedisStorageVo.getTagids();
            tagids.forEach((tagid) -> {
                TProjectTag tProjectTag = new TProjectTag();
                tProjectTag.setTagid(tagid);
                tProjectTag.setProjectid(id);
                tProjectTagMapper.insertSelective(tProjectTag);
            });
            //4.保存项目分类
            List<Integer> typeids = projectRedisStorageVo.getTypeids();
            typeids.forEach((typeid) -> {
                TProjectType tProjectType = new TProjectType();
                tProjectType.setProjectid(id);
                tProjectType.setTypeid(typeid);
                tProjectTypeMapper.insertSelective(tProjectType);
            });
        });

        //5.保存项目的回报档位信息
        List<TReturn> projectReturns = projectRedisStorageVo.getProjectReturns();
        projectReturns.forEach((projectReturn)->{
            projectReturn.setProjectid(id);
            tReturnMapper.insertSelective(projectReturn);
        });
        //6.项目保存成功，将redis中临时数据删除
        redisTemplate.delete(AppConstant.PROJECT_TEMP_CACHE_PREFIX+vo.getProjectToken());
    }

    //保存草稿
    @Override
    public void tempProjectToDb(BaseVo vo) {
        saveProject(vo,ProjectStatusEnume.DRAFT);
    }

    @Override
    public List<TProject> getAllProjects() {
        return tProjectMapper.selectByExample(null);
    }

    @Override
    public List<ProjectAllInfoVo> getAllProjectsInfos() {

        List<ProjectAllInfoVo> projectAllInfoVos = tProjectMapper.getAllProjectsInfos();
        return projectAllInfoVos;
    }

}
