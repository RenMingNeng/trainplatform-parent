package com.bossien.train.service.impl;

import com.bossien.train.dao.ex.CourseQuestionMapper;
import com.bossien.train.dao.tp.CourseInfoMapper;
import com.bossien.train.domain.CourseInfo;
import com.bossien.train.domain.ProjectCourse;
import com.bossien.train.domain.dto.CourseMessage;
import com.bossien.train.service.ICompanyCourseService;
import com.bossien.train.service.ICourseInfoService;
import com.bossien.train.service.ISequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 课程信息表
 * Created by Administrator on 2017/7/26.
 */
@Service(value = "courseInfoService")
public class CourseInfoServiceImpl implements ICourseInfoService {
    @Autowired
    private CourseInfoMapper courseInfoMapper;
    @Autowired
    private CourseQuestionMapper courseQuestionMapper;
    @Autowired
    private ICompanyCourseService companyCourseService;
    @Autowired
    private ISequenceService sequenceService;

    @Override
    public Integer selectCount(Map<String, Object> params) {
        return courseInfoMapper.selectCount(params);
    }

    @Override
    public List<Map<String, Object>> selectList(Map<String, Object> params) {
        return courseInfoMapper.selectList(params);
    }

    @Override
    public List<CourseInfo> selectAll() {
        return courseInfoMapper.selectAll();
    }

    /**
     * 通过课程主键id查询课程
     *
     * @param
     * @return
     */
    @Override
    public List<CourseInfo> selectCourseByIds(Map<String, Object> courseids) {
        return courseInfoMapper.selectCourseByIds(courseids);
    }

    /**
     * 通过课程id查询课程总数
     *
     * @param
     * @return
     */
    @Override
    public Integer selectCourseCountByIds(Map<String, Object> courseids) {
        return courseInfoMapper.selectCourseCountByIds(courseids);
    }
    /**
     * 通过课程id查询课程信息
     *
     * @param courseId
     * @return
     */
   @Override
   public  CourseInfo selectOne(String courseId){
       return courseInfoMapper.selectOne(courseId);
   }
    /**
     * 将projectInfo中的学时要求，必选题量与courseInfo中的数据组装到map中
     *
     * @param projectCourses
     * @param courseInfos
     * @return
     */
    @Override
    public List<Map<String, Object>> saveProjectCourseInfoDate(List<ProjectCourse> projectCourses, List<CourseInfo> courseInfos) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (CourseInfo courseInfo : courseInfos) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("courseNo", courseInfo.getCourseNo());                       //课程编号
            map.put("courseName", courseInfo.getCourseName());                   //课程名称
            map.put("classHour", courseInfo.getClassHour());                     //课时
            map.put("questionCount", courseInfo.getQuestionCount());             //题量
            //循环项目课程表去除每个课程的学时要求
            for (ProjectCourse projectCourse : projectCourses) {
                if (courseInfo.getCourseId().equals(projectCourse.getCourseId())) {
                    map.put("requirement", projectCourse.getRequirement());      //学时要求
                    map.put("select_count", projectCourse.getSelectCount());     //必选题量
                    break;
                }
            }
            list.add(map);
        }
        return list;
    }

    @Override
    public CourseInfo selectCourseInfoByCourseId(String courseId) {
        return courseInfoMapper.selectCourseInfoByCourseId(courseId);
    }

    /**
     * 根据courseTypeId或者courseName查询课程
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> selectCourseList(Map<String, Object> params) {
        return courseInfoMapper.selectCourseList(params);
    }

    /**
     * 根据courseTypeId或者courseName查询课程总条数
     *
     * @param params
     * @return
     */
    @Override
    public Integer selectCourseCount(Map<String, Object> params) {
        return courseInfoMapper.selectCourseCount(params);
    }

    /**
     * 课程下的总题量
     *
     * @param params
     * @return
     */
    @Override
    public int selectCourseQuestionCount(Map<String, Object> params) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("intCompanyId", params.get("companyId").toString());
        //根据companyId查询courseIds
        List<Map<String, Object>> companyCourses = companyCourseService.selectByCompanyId(param);
        List<String> courseIds = new ArrayList<String>();
        if (null != companyCourses && companyCourses.size() > 0) {
            for (Map map : companyCourses) {
                courseIds.add(map.get("intCourseId").toString());
            }
        }
        if (courseIds.size() == 0) {
            return 0;
        }
        param.put("courseIds", courseIds);
        return courseInfoMapper.selectCourseQuestionCount(param);
    }

    /**
     * 课程转移
     *
     * @param params
     */
    @Override
    public void courseMove(Map<String, Object> params) {
        //获取课程Ids集合
        String[] courseIds = (String[]) params.get("courseIds");
        for (String courseId : courseIds) {
            params.put("courseId", courseId);
            courseInfoMapper.update(params);
        }
    }

    @Override
    public int insertSelective(Map<String, Object> params) {
        return courseInfoMapper.insertSelective(params);
    }

    @Override
    public int insertMessage(CourseMessage courseMessage) {
        /*插入CouresInfo表*/
        Map<String, Object> map = new HashMap<>();
        map.put("id", sequenceService.generator());
        map.put("courseId", courseMessage.getCourseId());
        map.put("intCourseId", courseMessage.getCourseId());
        map.put("courseNo", courseMessage.getCourseCode());
        map.put("courseName", courseMessage.getCourseName());
        map.put("courseTypeId", courseMessage.getCourseTypeId());
        map.put("courseTypeName", courseMessage.getCourse_Type());
        map.put("questionCount", courseMessage.getCourseQuestionNumber());
        map.put("classHour", courseMessage.getCourseClassHour());
        map.put("createUser", courseMessage.getCreateUser());
        map.put("createTime", courseMessage.getDatCreateDate());
        map.put("operUser", courseMessage.getOperUser());
        map.put("operTime", courseMessage.getDatOperDate());
        courseInfoMapper.insertSelective(map);
        return 0;
    }

    @Override
    public int updateMessage(CourseMessage courseMessage) {
         /*修改*/
        Map<String, Object> map = new HashMap<>();
        map.put("courseId", courseMessage.getCourseId());
        map.put("intCourseId", courseMessage.getCourseId());
        map.put("courseNo", courseMessage.getCourseCode());
        map.put("courseName", courseMessage.getCourseName());
        map.put("courseTypeId", courseMessage.getCourseTypeId());
        map.put("questionCount", courseMessage.getCourseQuestionNumber());
        map.put("courseTypeName", courseMessage.getCourse_Type());
        map.put("classHour", courseMessage.getCourseClassHour());
        map.put("createUser", courseMessage.getCreateUser());
        map.put("create_time", courseMessage.getDatCreateDate());
        map.put("operUser", courseMessage.getOperUser());
        map.put("operTime", courseMessage.getDatOperDate());
        courseInfoMapper.updateByPrimaryKeySelective(map);
        return 0;
    }

    @Override
    public int insertBatch(List< Map<String, Object>> list) {
        return courseInfoMapper.insertBatch(list);
    }

    @Override
    public int updateBatch( List< Map<String, Object>> list) {
        return courseInfoMapper.updateBatch(list);
    }

    @Override
    public int updateByPrimaryKeySelective(Map<String, Object> params) {
        return courseInfoMapper.updateByPrimaryKeySelective(params);
    }

    @Override
    public int deleteByPrimaryKey(Map<String, Object> params) {
        return courseInfoMapper.deleteByPrimaryKey(params);
    }

    @Override
    public void deleteByCourseId(Map<String, Object> params) {
        courseInfoMapper.deleteByCourseId(params);
    }

    @Override
    public List<CourseInfo> selectCourses(List<String> courseIds) {
        return courseInfoMapper.selectCourses(courseIds);
    }
}
