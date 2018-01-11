package com.bossien.train.service.impl;

import com.bossien.train.dao.ex.CourseMapper;
import com.bossien.train.domain.Course;
import com.bossien.train.domain.dto.CourseMessage;
import com.bossien.train.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CourseServiceImpl implements ICourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<Course> selectList(Map<String, Object> params) {
        return courseMapper.selectList(params);
    }

    @Override
    public List<Course> selectAll(Map<String, Object> params) {
        return courseMapper.selectAll(params);
    }


    @Override
    public Integer selectCount(Map<String, Object> params) {
        return courseMapper.selectCount(params);
    }

    @Override
    public Course selectOne(Map<String, Object> params) {
        return courseMapper.selectOne(params);
    }

    @Override
    public Course selectId(Map<String, Object> params) {
        return courseMapper.selectId(params);
    }

    /**
     * 根据intTypeId或者courseName查询课程
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> selectCourseList(Map<String, Object> params) {
        return courseMapper.selectCourseList(params);
    }

    /**
     * 根据intTypeId或者courseName查询课程总条数
     *
     * @param params
     * @return
     */
    @Override
    public Integer selectCourseCount(Map<String, Object> params) {
        return courseMapper.selectCourseCount(params);
    }

    /**
     * 通过主键id查询单条
     *
     * @param intId
     * @return
     */
    @Override
    public Course selectById(String intId) {
        return courseMapper.selectById(intId);
    }

    @Override
    public int insertSelective(CourseMessage courseMessage) {
        Course course = new Course();
        course.setIntCompanyId(courseMessage.getCourseCompanyId());
        course.setChrSource(courseMessage.getCourseSource());
        course.setIntId(courseMessage.getCourseId());
        course.setVarCode(courseMessage.getCourseCode());
        course.setIntTypeId(courseMessage.getCourseTypeId());
        course.setVarName(courseMessage.getCourseName());
        course.setVarDesc(courseMessage.getCourseDesc());
        course.setIntClassHour(courseMessage.getCourseClassHour());
        course.setVarCoverInfo(courseMessage.getCourseCoverInfo());
        course.setChrType(courseMessage.getCourse_Type());
        course.setChrPlatformType(courseMessage.getCoursePlatformType());
        course.setChrStatus(courseMessage.getCourseStatus());
        course.setDatCreateDate(courseMessage.getDatCreateDate());
        course.setDatOperDate(courseMessage.getDatOperDate());
        course.setVarCreateUser(courseMessage.getCreateUser());
        course.setVarOperUser(courseMessage.getOperUser());
        courseMapper.insertSelective(course);
        return 0;
    }

    @Override
    public int updateByVarCodeSelective(CourseMessage courseMessage) {
        Course course = new Course();
        course.setIntId(courseMessage.getCourseId());
        course.setIntCompanyId(courseMessage.getCourseCompanyId());
        course.setChrSource(courseMessage.getCourseSource());
        course.setVarCode(courseMessage.getCourseCode());
        course.setIntTypeId(courseMessage.getCourseTypeId());
        course.setVarName(courseMessage.getCourseName());
        course.setVarDesc(courseMessage.getCourseDesc());
        course.setVarCoverInfo(courseMessage.getCourseCoverInfo());
        course.setIntClassHour(courseMessage.getCourseClassHour());
        course.setChrType(courseMessage.getCourse_Type());
        course.setChrPlatformType(courseMessage.getCoursePlatformType());
        course.setChrStatus(courseMessage.getCourseStatus());
        course.setDatCreateDate(courseMessage.getDatCreateDate());
        course.setDatOperDate(courseMessage.getDatOperDate());
        course.setVarCreateUser(courseMessage.getCreateUser());
        course.setVarOperUser(courseMessage.getOperUser());
        courseMapper.updateByVarCodeSelective(course);
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(Map<String, Object> params) {
        return courseMapper.deleteByPrimaryKey(params);
    }

    @Override
    public int insertBatch(List<Course> list) {
        return courseMapper.insertBatch(list);
    }

    @Override
    public int updateBatch(List<Course> list) {
        return courseMapper.updateBatch(list);
    }

}
