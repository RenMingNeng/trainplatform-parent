package com.bossien.train.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.bossien.train.dao.ap.SubjectCourseMapper;
import com.bossien.train.domain.SubjectCourse;
import com.bossien.train.service.ISequenceService;
import com.bossien.train.service.ISubjectCourseService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.bossien.train.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectCourseServiceImpl implements ISubjectCourseService {


    @Autowired
    private SubjectCourseMapper subjectCourseMapper;
    @Autowired
    private ISequenceService sequenceService;

    @Override
    public int deleteByPrimaryKey(String intId) {
        return subjectCourseMapper.deleteByPrimaryKey(intId);
    }

    @Override
    public int deleteByTrainSubjectId(String trainSubjectId) {
        return subjectCourseMapper.deleteByTrainSubjectId(trainSubjectId);
    }

    @Override
    public int insert(SubjectCourse record) {
        return subjectCourseMapper.insert(record);
    }

    @Override
    public int insertSelective(SubjectCourse record) {
        return subjectCourseMapper.insertSelective(record);
    }

    @Override
    public SubjectCourse selectByPrimaryKey(String intId) {
        return subjectCourseMapper.selectByPrimaryKey(intId);
    }

    @Override
    public int updateByPrimaryKeySelective(SubjectCourse record) {
        return subjectCourseMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SubjectCourse record) {
        return subjectCourseMapper.updateByPrimaryKey(record);
    }

    @Override
    public int deleteByCourseId(Map<String, Object> map) {
        return subjectCourseMapper.deleteByCourseId(map);
    }

    @Override
    public List<String> selectCourseIds(String subjectId) {
        return subjectCourseMapper.selectCourseIds(subjectId);
    }

    /**
     * 批量添加培训主题和课程的关联关系
     *
     * @param params
     * @return
     */
    @Override
    public int saveTrainSubjectCourse(Map<String, Object> params) {
        String[] courseIds = (String[]) params.get("courseIds");
        int count = 0;
        //获取课程Ids集合
        if (courseIds == null) {
            return count;
        }
        List<SubjectCourse> subjectCourses = new ArrayList<SubjectCourse>();

        for (String courseId : courseIds) {
            SubjectCourse subjectCourse = new SubjectCourse();
            subjectCourse.setIntId(sequenceService.generator());
            subjectCourse.setIntTrainSubjectId(params.get("trainSubjectId").toString());
            subjectCourse.setIntCourseId(courseId);
            subjectCourses.add(subjectCourse);
        }
        if (subjectCourses.size() > 0) {
            //批量插入项目课程
            count = subjectCourseMapper.insertBatch(subjectCourses);
        }
        return count;
    }
}
