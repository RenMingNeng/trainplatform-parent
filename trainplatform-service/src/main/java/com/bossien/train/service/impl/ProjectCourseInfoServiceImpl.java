package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.ProjectCourseInfoMapper;
import com.bossien.train.domain.CourseInfo;
import com.bossien.train.domain.ProjectBasic;
import com.bossien.train.domain.ProjectCourse;
import com.bossien.train.domain.ProjectCourseInfo;
import com.bossien.train.domain.User;
import com.bossien.train.service.IBaseService;
import com.bossien.train.service.ICourseInfoService;
import com.bossien.train.service.IProjectCourseInfoService;
import com.bossien.train.service.IProjectCourseService;
import com.bossien.train.util.JsonUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectCourseInfoServiceImpl implements IProjectCourseInfoService {

    @Autowired
    private ProjectCourseInfoMapper projectCourseInfoMapper;

    @Override
    public List<Map<String, Object>> selectList(Map<String, Object> params) {

        return projectCourseInfoMapper.selectList(params);
    }

    @Override
    public int insertBatch(List<Map<String, Object>> item){
        return projectCourseInfoMapper.insertBatch(item);
    }

    @Override
    public int deleteBatch(Map<String, Object> params) {
        return projectCourseInfoMapper.deleteBatch(params);
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {

        return projectCourseInfoMapper.selectCount(params);
    }

    @Override
    public void saveProjectCourseInfo(ProjectBasic projectBasic) {

    }

    @Override
    public ProjectCourseInfo selectOne(Map<String, Object> params) {
        return projectCourseInfoMapper.selectOne(params);
    }

    @Override
    public Integer update(ProjectCourseInfo projectCourseInfo) {
        return projectCourseInfoMapper.update(projectCourseInfo);
    }

    @Override
    public Integer insert(ProjectCourseInfo projectCourseInfo) {
        return projectCourseInfoMapper.insert(projectCourseInfo);
    }

    @Override
    public List<String> selectIds(Map<String, Object> params) {
        return projectCourseInfoMapper.selectIds(params);
    }

    @Override
    public List<String> selectUserIds(Map<String, Object> params) {
        return projectCourseInfoMapper.selectUserIds(params);
    }

    @Override
    public List<String> selectIdList(Map<String, Object> params) {
        return projectCourseInfoMapper.selectIdList(params);
    }

    @Override
    public List<Map<String, Object>> selectCourseList(Map<String, Object> params) {
        return projectCourseInfoMapper.selectCourseList(params);
    }

}
