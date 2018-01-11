package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.ProjectCourseInfoMapper;
import com.bossien.train.dao.tp.ProjectCourseMapper;
import com.bossien.train.domain.*;
import com.bossien.train.service.*;
import com.bossien.train.util.JsonUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;


/**
 * 项目课程表
 * Created by Administrator on 2017/7/26.
 */
@Service
public class ProjectCourseServiceImpl implements IProjectCourseService {

    @Autowired
    private ProjectCourseMapper projectCourseMapper;

    @Autowired
    private IBaseService baseService;

    @Autowired
    private ICourseInfoService courseInfoService;
    @Autowired
    private IProjectBasicService projectBasicService;
    @Autowired
    private ProjectCourseInfoMapper projectCourseInfoMapper;

    /**
     * 根据projectId和roleId查询相关课程
     *
     * @param map
     * @return
     */
    @Override
    public List<ProjectCourse> selectByProjectIdAndRoleId(Map<String, Object> map) {
        return projectCourseMapper.selectByProjectIdAndRoleId(map);
    }

    /**
     * 批量添加项目课程信息
     *
     * @param params
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    public List<String> saveBatch(Map<String, Object> params, User user) throws Exception {
        String roles = params.get("roles").toString();
        Class<Map> clazz = Map.class;
        List<String> courseIds = new ArrayList<>();
        //获取课程Ids数组
        String[] courseIdArray = (String[])params.get("courseIds");
        for (String courseId: courseIdArray) {
            courseIds.add(courseId);
        }
        if(CollectionUtils.isEmpty(courseIds)){
            return courseIds;
        }
        //根据项目Id查询课程集合
        List<String> courseIds_ = projectCourseMapper.selectCourseIds(params);
        //如果表中有该课程就移除
        if (courseIds_ != null && courseIds_.size() > 0) {
            courseIds.removeAll(courseIds_);
        }
        //集合为空就返回
        if (courseIds == null || courseIds.size() < 1) {
            return courseIds;
        }
        Map<String, Object> coursesMap = new HashedMap();
        coursesMap.put("courseids", courseIds);
        //获取课程信息集合
        List<CourseInfo> courseList = courseInfoService.selectCourseByIds(coursesMap);
        if (courseList == null || courseList.size() < 1) {
            return courseIds;
        }
        //获取角色信息集合
        List<Map> roleList = JsonUtils.joinToList(roles, clazz);
        //创建项目课程集合
        List<ProjectCourse> item = null;
        ProjectCourse projectCourse = null;
        for (Map roleMap : roleList) {
            item = new ArrayList<>();
            String roleId = roleMap.get("roleId").toString();
            String roleName = roleMap.get("roleName").toString();
            for (CourseInfo courseInfo : courseList) {
                //创建项目课程
                projectCourse = new ProjectCourse();
                projectCourse = (ProjectCourse) baseService.build(user, projectCourse);
                projectCourse.setProjectId(params.get("projectId").toString());
                projectCourse.setCourseId(courseInfo.getCourseId());
                projectCourse.setCourseNo(courseInfo.getCourseNo());
                projectCourse.setCourseName(courseInfo.getCourseName());
                projectCourse.setRoleId(roleId);
                projectCourse.setRoleName(roleName);
                projectCourse.setRequirement(courseInfo.getClassHour());
                projectCourse.setClassHour(courseInfo.getClassHour());
                projectCourse.setQuestionCount(courseInfo.getQuestionCount());
                projectCourse.setSelectCount(0);
                //添加项目课程到集合
                item.add(projectCourse);
            }
            if (item != null && item.size() > 0) {
                //批量插入项目课程
                projectCourseMapper.insertBatch(item);
            }
        }
        return courseIds;
    }


    @Override
    public int deleteBatch(Map<String, Object> params) {
        return projectCourseMapper.deleteBatch(params);
    }

    @Override
    public List<String> selectCourseIds(Map<String, Object> param) {
        return projectCourseMapper.selectCourseIds(param);
    }

    @Cacheable(value = "ProjectCourseCache#(60 * 60)", key = "'selectCourseIds'.concat('_').concat(#projectId).concat('_').concat(#roleId)")
    @Override
    public List<String> selectCourseIds(String projectId, String roleId) {
        Map<String, Object> param_ = new HashedMap();
        param_.put("projectId", projectId);
        param_.put("roleId", roleId);
        return projectCourseMapper.selectCourseIds(param_);
    }

    @Override
    public void saveBatch_Public(Map<String, Object> params, User user) throws Exception {
        String _courseIds = (String) params.get("course_ids");
        String projectBasicId = (String) params.get("project_id");
        ProjectBasic projectBasic = projectBasicService.selectById(projectBasicId);
        if (null == projectBasic) {
            return;
        }
        //获取课程Ids集合
        List<String> courseIds = Arrays.asList(_courseIds.split(","));
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("courseIds", courseIds);
        param.put("project_id", projectBasicId);
        //先做批量删除操作（防止重复插入数据）
        projectCourseMapper.deleteBatchByCourseIds(param);
        Map<String, Object> coursesMap = new HashedMap();
        coursesMap.put("courseids", courseIds);
        //获取课程信息集合
        List<CourseInfo> courseList = courseInfoService.selectCourseByIds(coursesMap);
        //创建项目课程集合
        List<ProjectCourse> item = new ArrayList<ProjectCourse>();
        for (CourseInfo courseInfo : courseList) {
            //创建项目课程
            ProjectCourse projectCourse = new ProjectCourse();
            projectCourse = (ProjectCourse) baseService.build(user, projectCourse);
            projectCourse.setProjectId(projectBasic.getId());
            projectCourse.setCourseId(courseInfo.getCourseId());
            projectCourse.setCourseNo(courseInfo.getCourseNo());
            projectCourse.setCourseName(courseInfo.getCourseName());
            projectCourse.setRoleId("-1");
            projectCourse.setRoleName("默认角色");
            projectCourse.setRequirement(courseInfo.getClassHour());
            projectCourse.setClassHour(courseInfo.getClassHour());
            projectCourse.setQuestionCount(courseInfo.getQuestionCount());
            projectCourse.setSelectCount(0);
            //添加项目课程到集合
            item.add(projectCourse);
        }
        if (item != null && item.size() > 0) {
            //批量插入项目课程
            projectCourseMapper.insertBatch(item);
        }
    }

    @Override
    public List<Map<String, Object>> selectProjectCourseInfo(Map<String, Object> params) {
        return projectCourseMapper.selectProjectCourseInfo(params);
    }

    @Override
    public Map<String, Object> selectProjectCourseMap(Map<String, Object> params) {
        return projectCourseMapper.selectProjectCourseMap(params);
    }

    @Override
    public List<Map<String, Object>> selectList(Map<String, Object> params) {
        return projectCourseMapper.selectList(params);
    }

    /**
     * 学员项目详情中的课程数量统计
     *
     * @param params
     * @return
     */
    @Override
    public int selectCourseCount(Map<String, Object> params) {
        return projectCourseMapper.selectCourseCount(params);
    }

    @Override
    public int courseCount(Map<String, Object> params) {
        return projectCourseMapper.courseCount(params);
    }

    @Override
    public int selectCount(Map<String, Object> params) {
        return projectCourseMapper.selectCount(params);
    }

    /**
     * 根据projectId和roleIds确认数据是否最新
     *
     * @param params
     */
    @Override
    public void confirmProjectCourse(Map<String, Object> params) {
        List<String> ids = projectCourseMapper.selectIds(params);
        if (ids != null && ids.size() > 0) {
            for (String id : ids) {
                projectCourseMapper.delete(id);
            }
        }

    }

    /**
     * 根据projectId和courseId修改
     *
     * @param params
     * @return
     */
    @Override
    public int update(Map<String, Object> params) {
        return projectCourseMapper.update(params);
    }

    /**
     * 根据projectId删除
     *
     * @param params
     * @return
     */
    @Override
    public int deleteProjectId(Map<String, Object> params) {
        return projectCourseMapper.deleteProjectId(params);
    }

    @Override
    public void deleteProjectCourse(Map<String, Object> params) {
        projectCourseMapper.deleteProjectCourse(params);
    }

    @Override
    public List<Map<String, Object>> selectProjectCourseList(Map<String, Object> params) {
        List<Map<String, Object>> projectCourseRoles = new ArrayList<Map<String, Object>>();
        //params.put("courseIds",projectCourseMapper.selectProjectCourseIds(params));
        List<Map<String, Object>> projectCourse = projectCourseMapper.selectProjectCourse(params);
        List<Map<String, Object>> roleCourse = null;

        for (Map<String, Object> map : projectCourse) {
            Map<String, Object> courseRoles = new HashMap<String, Object>();
            courseRoles.put("id", map.get("id").toString());
            courseRoles.put("project_id", map.get("projectId").toString());
            courseRoles.put("course_id", map.get("courseId").toString());
            courseRoles.put("course_no", map.get("courseNo").toString());
            courseRoles.put("course_name", map.get("courseName").toString());
            courseRoles.put("question_count", map.get("questionCount").toString());
            courseRoles.put("class_hour", map.get("classHour").toString());

            roleCourse = projectCourseMapper.selectProjectRoleAndCourse(map);

            for (int i = 0; i < roleCourse.size(); i++) {
                courseRoles.put("role_id" + i, roleCourse.get(i).get("roleId").toString());
                courseRoles.put("requirement" + i, roleCourse.get(i).get("requirement").toString());
                courseRoles.put("select_count" + i, roleCourse.get(i).get("selectCount").toString());
            }
            projectCourseRoles.add(courseRoles);
        }
        return projectCourseRoles;
    }

    /**
     * 高级设置中查询项目课程总条数
     *
     * @param params
     * @return
     */
    @Override
    public int selectProjectCourseCount(Map<String, Object> params) {
        return projectCourseMapper.selectProjectCourseCount(params);
    }

    @Override
    public int insertBatch(List<ProjectCourse> item) {
        return projectCourseMapper.insertBatch(item);
    }

    @Override
    public void deleteBatchByCourseIds(Map<String, Object> param) {
        projectCourseMapper.deleteBatchByCourseIds(param);
    }

    @Override
    public List<String> selectRoleIds(String projectId) {
        return projectCourseMapper.selectRoleIds(projectId);
    }

    /**
     * 根据projectId查询课程集合
     *
     * @param params
     * @return
     */
    @Override
    public List<ProjectCourse> selectProjectCourses(Map<String, Object> params) {
        return projectCourseMapper.selectProjectCourses(params);
    }

    @Override
    public int insertCourseInfo(CourseInfo courseInfo) {
        return 0;
    }

    @Override
    public List<Map<String,Object>> selectTotalQuestionByProjectId( String projectId) {
        return projectCourseMapper.selectTotalQuestionByProjectId(projectId);
    }

    @Override
    public void updateQuestionCount(String projectId) {
        Map<String, Object> params = new HashedMap();
        params.put("projectId",projectId);
        //获取该项目中的课程Ids
        List<String> courseIds = projectCourseMapper.selectCourseIdList(params);
        if(courseIds != null && courseIds.size()>0){
            //获取所有courseInfo集合
            List<CourseInfo> list = courseInfoService.selectCourses(courseIds);
            ProjectCourseInfo projectCourseInfo = new ProjectCourseInfo();
            projectCourseInfo.setProjectId(projectId);
            for (CourseInfo courseInfo: list) {
                params.put("courseId",courseInfo.getCourseId());
                params.put("questionCount",courseInfo.getQuestionCount());
                //修改project_course的question_count
                projectCourseMapper.updateQuestionCount(params);
                //修改project_course_info的question_count
                projectCourseInfo.setCourseId(courseInfo.getCourseId());
                projectCourseInfo.setTotalQuestion(courseInfo.getQuestionCount());
                projectCourseInfoMapper.update(projectCourseInfo);
            }
        }
    }
}
